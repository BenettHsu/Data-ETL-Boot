package cn.codeben.task;

import com.citms.config.PdamProperties;
import com.citms.utils.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class AbstractTask implements InterfaceTask {

    @Autowired
    protected PdamProperties pdamProperties;

    /**
     * @description: 重试机制
     */
    @Override
    public void handle(String taskName,Object param) {
        AtomicInteger currentRetryCount = new AtomicInteger(0);
        int totalRetryCount = pdamProperties.getExceptionRetryCount();
        while (currentRetryCount.get() <= totalRetryCount){
            try {
                realHandle(param);
                return;
            }catch (Exception e){
                retryHandleWhenException(currentRetryCount,taskName,e.getMessage(),param);
            }
        }
    }

    @Override
    public void notImplementRealHandleMethod() {
        log.error("not implement real handle method!");
    }

    public void retryHandleWhenException(AtomicInteger currentRetryCount, String taskName,String errorMsg,Object param){
        //下次重试时间 固定 200ms
        long nextRetryTime = pdamProperties.getExceptionRetryTimeGap();
        if (currentRetryCount.incrementAndGet() > pdamProperties.getExceptionRetryCount()){
            log.error("[全部失败] => {}任务 => {}次",taskName, pdamProperties.getExceptionRetryCount());
            extraHandleWhenAllRetryFailed(errorMsg,param);
        }else {
            log.error("[重试] => {}任务 => 将在{}ms后第{}次重试，共{}次",taskName,nextRetryTime,currentRetryCount.get(), pdamProperties.getExceptionRetryCount());
        }
        try {
            Thread.sleep(nextRetryTime * MILLI_SECOND);
        } catch (InterruptedException e) {
            log.error("thread sleep throw exception ,{}",e.getMessage());
        }
    }

    public void extraHandleWhenAllRetryFailed(String errorMsg,Object param){}

    @Data
    public class Timer {
        private String taskName;
        private Long startTime;
        private String startTimeStr;
        private Long endTime;
        private String endTimeStr;

        Timer(String taskName) {
            this.taskName = taskName;
        }

        public Timer start() {
            startTime = System.currentTimeMillis();
            startTimeStr = DateUtil.getCurrentStr();
            return this;
        }

        public void end(Object data) {
            endTime = System.currentTimeMillis();
            endTimeStr = DateUtil.getCurrentStr();
            log.info("[FINISH] => {}任务 => 开始:{},结束:{} => 用时:{}ms => 数据:{}", taskName,startTimeStr,endTimeStr,endTime - startTime,data);
        }
    }
}

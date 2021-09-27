package cn.codeben.task;


import cn.codeben.config.EtlBootProperties;
import cn.codeben.enums.LogLevelEnum;
import cn.codeben.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuben
 */
@Slf4j
public abstract class AbstractTask implements InterfaceTask {

    @Autowired
    protected EtlBootProperties properties;

    /**
     * @description: 重试机制
     */
    @Override
    public void handle(String taskName,Object param) {
        AtomicInteger currentRetryCount = new AtomicInteger(0);
        int totalRetryCount = properties.getExceptionHandleProperties().getExceptionRetryCount();
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
        long nextRetryTime = properties.getExceptionHandleProperties().getExceptionRetryTimeGap();
        if (currentRetryCount.incrementAndGet() > properties.getExceptionHandleProperties().getExceptionRetryCount()){
            log.error("[全部失败] => {}任务 => {}次",taskName, properties.getExceptionHandleProperties().getExceptionRetryCount());
            extraHandleWhenAllRetryFailed(errorMsg,param);
        }else {
            log.error("[重试] => {}任务 => 将在{}ms后第{}次重试，共{}次",taskName,nextRetryTime,currentRetryCount.get(), properties.getExceptionHandleProperties().getExceptionRetryCount());
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
        private LogLevelEnum logLevelEnum;

        Timer(String taskName) {
            this.taskName = taskName;
        }

        public Timer start() {
            startTime = System.currentTimeMillis();
            startTimeStr = DateUtil.getNowStr();
            return this;
        }

        public Timer setLogLevel(LogLevelEnum logLevelEnum){
            this.logLevelEnum = logLevelEnum;
            return this;
        }

        public void end(Object data) {
            endTime = System.currentTimeMillis();
            endTimeStr = DateUtil.getNowStr();
            if (logLevelEnum == null){
                logLevelEnum = LogLevelEnum.DEBUG;
            }
            String logMsg = String.format("[FINISH] => {}任务 => 开始:{},结束:{} => 用时:{}ms => 数据:{}", taskName,startTimeStr,endTimeStr,endTime - startTime,data);
            switch (logLevelEnum){
                case DEBUG: log.debug(logMsg);break;
                case INFO: log.info(logMsg);break;
                case WARN: log.warn(logMsg);break;
                case ERROR: log.error(logMsg);break;
                default: log.error("not support this log level in cn.codeben.task.AbstractTask.ent()");
            }
        }
    }
}

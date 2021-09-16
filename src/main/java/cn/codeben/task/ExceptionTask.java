package cn.codeben.task;

import cn.codeben.pojo.dto.ExceptionTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class ExceptionTask extends AbstractTask {
    @Resource(name = "exceptionTaskExecutor")
    private Executor executor ;
    private final String TASK_NAME = "[异常入库]";

    /**
     * @description: 异常入库
     */
    public void exceptionTask(ExceptionTaskDto dto){
        executor.execute(()->{
            Timer timer = new Timer(TASK_NAME).start();
            handle(TASK_NAME,dto);
            timer.end(dto);
        });
    }

    /**
     * @description: 真正的处理入口
     */
    @Override
    public void realHandle(Object param) {
        ExceptionTaskDto dto = (ExceptionTaskDto) param;
        //示例:直接打印错误日志存储
        log.error("[异常信息存储]{}任务: info: {}",TASK_NAME,dto.toString());
    }

    @Override
    public void extraHandleWhenAllRetryFailed(String errorMsg,Object param) {
        log.error("[丢弃]{}任务 ,errorMsg:{},param:{}",TASK_NAME,errorMsg,param);

    }
}

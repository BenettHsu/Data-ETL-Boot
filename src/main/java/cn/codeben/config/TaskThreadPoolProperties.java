package cn.codeben.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务线程配置
 */
@Data
@Slf4j
@Configuration
public class TaskThreadPoolProperties {
    private final String SOURCE_TASK_NAME = "SourceTask";
    /**
     * sourceTask线程池 核心线程容量
     */
    private int sourceTaskThreadPoolCoreSize = 5;
    /**
     * sourceTask线程池 最大线程容量
     */
    private int sourceTaskThreadPoolMaxSize = 10;
    /**
     * sourceTask队列容量
     */
    private int sourceTaskQueueSize = 10;

    private final String FLATMAP_TASK_NAME = "FlatMapTask";
    /**
     * flatMapTask线程池 核心线程容量
     */
    private int flatMapTaskThreadPoolCoreSize = 5;
    /**
     * flatMapTask线程池 最大线程容量
     */
    private int flatMapTaskThreadPoolMaxSize = 10;
    /**
     * flatMapTask阻塞队列容量
     */
    private int flatMapTaskQueueSize = 10;

    private final String REDUCE_TASK_NAME = "ReduceTask";
    /**
     * reduceTask线程池 核心线程容量
     */
    private int reduceTaskThreadPoolCoreSize = 5;
    /**
     * SourceTask线程池 最大线程容量
     */
    private int reduceTaskThreadPoolMaxSize = 10;
    /**
     * SourceTask队列容量
     */
    private int reduceTaskQueueSize = 10;

    private final String SINK_TASK_NAME = "SinkTask";
    /**
     * sinkTask线程池 核心线程容量
     */
    private int sinkTaskThreadPoolCoreSize = 5;
    /**
     * sinkTask线程池 最大线程容量
     */
    private int sinkTaskThreadPoolMaxSize = 10;
    /**
     * sinkTask队列容量
     */
    private int sinkTaskQueueSize = 10;

    private final String MONITOR_TASK_NAME = "MonitorTask";
    /**
     * monitor线程池 核心线程容量
     */
    private int monitorTaskThreadPoolCoreSize = 1;
    /**
     * monitor线程池 最大线程容量
     */
    private int monitorTaskThreadPoolMaxSize = 1;
    /**
     * monitor队列容量
     */
    private int monitorTaskQueueSize = 0;

    private final String EXCEPTION_TASK_NAME = "ExceptionTask";
    /**
     * exception线程池 核心线程容量
     */
    private int exceptionTaskThreadPoolCoreSize = 1;
    /**
     * exception线程池 最大线程容量
     */
    private int exceptionTaskThreadPoolMaxSize = 3;
    /**
     * exception队列容量
     */
    private int exceptionTaskQueueSize = 1000;

    @Bean(name = "sourceTaskExecutor")
    Executor sourceTheadPool() {
        ThreadPoolTaskExecutor sourceTask = getThreadPoolTaskExecutor(
                sourceTaskThreadPoolCoreSize, sourceTaskThreadPoolMaxSize, sourceTaskQueueSize
                , null, SOURCE_TASK_NAME
        );

        return sourceTask;
    }

    @Bean(name = "flatMapTaskExecutor")
    Executor flatMapTheadPool() {
        return getThreadPoolTaskExecutor(
                flatMapTaskThreadPoolCoreSize,flatMapTaskThreadPoolMaxSize,flatMapTaskQueueSize
                ,null,FLATMAP_TASK_NAME
        );
    }

    @Bean(name = "reduceTaskExecutor")
    Executor reduceTheadPool() {
        return getThreadPoolTaskExecutor(
                reduceTaskThreadPoolCoreSize,reduceTaskThreadPoolMaxSize,reduceTaskQueueSize
                ,null,REDUCE_TASK_NAME
        );
    }

    @Bean(name = "sinkTaskExecutor")
    Executor sinkTheadPool() {
        return getThreadPoolTaskExecutor(
                sinkTaskThreadPoolCoreSize,sinkTaskThreadPoolMaxSize,sinkTaskQueueSize
                ,null,SINK_TASK_NAME
        );
    }

    @Bean(name = "exceptionTaskExecutor")
    Executor exceptionTheadPool() {
        return getThreadPoolTaskExecutor(
                exceptionTaskThreadPoolCoreSize,exceptionTaskThreadPoolMaxSize,exceptionTaskQueueSize
                ,new ThreadPoolExecutor.AbortPolicy(),EXCEPTION_TASK_NAME
        );
    }

    @Bean(name = "monitorTaskExecutor")
    Executor monitorTheadPool() {
        return getThreadPoolTaskExecutor(
                monitorTaskThreadPoolCoreSize,monitorTaskThreadPoolMaxSize,monitorTaskQueueSize
                ,null,MONITOR_TASK_NAME
        );
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(
            int core, int max, int queue, RejectedExecutionHandler handler, String taskName){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(core);
        taskExecutor.setMaxPoolSize(max);
        taskExecutor.setQueueCapacity(queue);
        if(handler == null){
            taskExecutor.setRejectedExecutionHandler((r, executor) -> {
                        if (!executor.isShutdown()) {
                            try {
                                executor.getQueue().put(r);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
            );
        }else {
            taskExecutor.setRejectedExecutionHandler(handler);
        }
        taskExecutor.setThreadNamePrefix(String.format("%s-Thread-",taskName));
        taskExecutor.initialize();
        log.info("[{}] Thread Poll initial complete : coreSize:[{}],maxSize:[{}],queueSize:[{}]",taskName,core,max,queue);
        return taskExecutor;
    }

    public void printInitialLog(String taskName,Integer coreSize,Integer maxSize,Integer queueSize){
        log.info("[{}] Thread Poll initial complete : coreSize:[{}],maxSize:[{}],queueSize:[{}]",taskName,coreSize,maxSize,queueSize);
    }
}

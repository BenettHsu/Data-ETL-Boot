package cn.codeben.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Ben
 */
@Component
@ConfigurationProperties("etl-boot")
@EnableConfigurationProperties({TaskThreadPoolProperties.class,ExceptionHandleProperties.class})
@Data
@Slf4j
public class EtlBootProperties {

    private TaskThreadPoolProperties taskThreadPoolProperties;

    private ExceptionHandleProperties exceptionHandleProperties;


    EtlBootProperties(TaskThreadPoolProperties taskThreadPoolProperties,ExceptionHandleProperties exceptionHandleProperties){
        this.taskThreadPoolProperties = taskThreadPoolProperties;
        this.exceptionHandleProperties = exceptionHandleProperties;
    }


}

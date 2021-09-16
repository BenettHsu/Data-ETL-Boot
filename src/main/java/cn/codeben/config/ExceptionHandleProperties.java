package cn.codeben.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * 异常处理配置
 * 目前ETL-boot 重试使用固定模式 ，后续会引进Spring-retry框架进行升级
 */
@Data
@Configuration
public class ExceptionHandleProperties {
    /**
     * 最大重试次数
     */
    private int exceptionRetryCount = 3;
    /**
     * 重试间隔ms 固定
     */
    private int exceptionRetryTimeGap = 200;

}

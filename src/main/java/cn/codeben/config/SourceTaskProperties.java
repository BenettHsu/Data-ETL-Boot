package cn.codeben.config;

import lombok.Data;

/**
 * SourceTask配置
 * @author Ben
 */
@Data
public class SourceTaskProperties {
    /**
     * 下发数据每批数据量大小
     */
    private Integer toNextPerSize = 100;
}

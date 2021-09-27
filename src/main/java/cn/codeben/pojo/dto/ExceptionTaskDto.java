package cn.codeben.pojo.dto;

import cn.codeben.enums.ExceptionTaskEnum;
import lombok.Data;

/**
 * @author xuben
 */
@Data
public class ExceptionTaskDto extends CommonTaskDto{
    ExceptionTaskEnum exceptionTaskEnum;
    Object data;

    public ExceptionTaskDto(ExceptionTaskEnum exceptionTaskEnum,Object data) {
        super(new StringBuilder(exceptionTaskEnum.getDesc()));
        this.data = data;

    }
}

package cn.codeben.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常枚举类型
 */
public enum ExceptionTaskEnum {
    DATA_EXTRACT_EXCEPTION("dataExtractException","数据转换异常");
    private String type;
    private String desc;

    ExceptionTaskEnum(String type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getType() {
        return this.type;
    }
}

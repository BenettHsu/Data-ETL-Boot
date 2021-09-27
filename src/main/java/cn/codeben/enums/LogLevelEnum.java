package cn.codeben.enums;

/**
 * @author xuben
 */

public enum LogLevelEnum {
    DEBUG("DEBUG","DEBUG"),
    INFO("INFO","INFO"),
    WARN("WARN","WARN"),
    ERROR("ERROR","ERROR");
    private String type;
    private String desc;

    LogLevelEnum(String type,String desc){
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

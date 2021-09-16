package cn.codeben.task;


public interface InterfaceTask {

    long MILLI_SECOND = 1L;


    void handle(String taskName,Object param);

    default void realHandle(Object param) throws InterruptedException {
        notImplementRealHandleMethod();
    }

    void notImplementRealHandleMethod();

}

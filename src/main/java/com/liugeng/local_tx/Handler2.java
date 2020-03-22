package com.liugeng.local_tx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

@Slf4j
public class Handler2 implements Rollback, Task {

    private File file2;
    private boolean needError = false;

    public void setNeedError(boolean needError) {
        this.needError = needError;
    }

    @Override
    public void rollback(Object TxContext, Throwable e) throws Throwable {
        log.warn("任务失败，task2需要回滚");
        try {
            FileUtils.forceDelete(file2);
        } catch (Exception ex) {
            log.error("task2回滚失败", ex);
        }
    }

    @Override
    public void doTask(Object TxContext) throws Throwable {
        log.info("开始task2, 创建task2文件");
        file2 = new File("task2");
        FileUtils.write(file2, "我是task2", Charset.defaultCharset());
        if (needError) {
            throw new Exception("我是一个错误");
        }
    }
}

package com.liugeng.local_tx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

@Slf4j
public class Handler1 extends AbstractTxHandler {

    private File file1;
    private boolean needError = false;

    @Override
    public void rollback(Throwable e) {
        log.warn("任务失败，task1需要回滚");
        try {
            FileUtils.forceDelete(file1);
        } catch (Exception ex) {
            log.error("task1回滚失败", ex);
        }
    }

    @Override
    public void doTask() throws Exception {
        log.info("开始task1, 创建task1文件");
        file1 = new File("task1");
        FileUtils.write(file1, "我是task1", Charset.defaultCharset());
        if (needError) {
            throw new Exception("我是一个错误");
        }
    }

    public void setNeedError(boolean needError) {
        this.needError = needError;
    }
}

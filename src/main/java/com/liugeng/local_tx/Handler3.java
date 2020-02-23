package com.liugeng.local_tx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

@Slf4j
public class Handler3 extends AbstractTxHandler {

    private File file3;
    private boolean needError = false;

    @Override
    public void rollback(Throwable e) {
        log.warn("任务失败，task3需要回滚");
        try {
            FileUtils.forceDelete(file3);
        } catch (Exception ex) {
            log.error("task3回滚失败", ex);
        }
    }

    @Override
    public void doTask() throws Exception {
        log.info("开始task3, 创建task3文件");
        file3 = new File("task3");
        FileUtils.write(file3, "我是task3", Charset.defaultCharset());
        if (needError) {
            throw new Exception("我是一个错误");
        }
    }

    public void setNeedError(boolean needError) {
        this.needError = needError;
    }
}

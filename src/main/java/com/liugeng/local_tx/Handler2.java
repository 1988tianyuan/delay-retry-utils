package com.liugeng.local_tx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

@Slf4j
public class Handler2 extends AbstractTxHandler {

    private File file2;
    private boolean needError = false;

    @Override
    public void rollback(Throwable e) {
        log.warn("任务失败，task2需要回滚");
        try {
            FileUtils.forceDelete(file2);
        } catch (Exception ex) {
            log.error("task2回滚失败", ex);
        }
    }

    @Override
    public void doTask() throws Exception {
        log.info("开始task2, 创建task2文件");
        file2 = new File("task2");
        FileUtils.write(file2, "我是task2", Charset.defaultCharset());
        if (needError) {
            throw new Exception("我是一个错误");
        }
    }

    public void setNeedError(boolean needError) {
        this.needError = needError;
    }
}

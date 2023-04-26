package io.quarkiverse.xxl.job.runtime.runtime;

import com.xxl.job.core.executor.XxlJobExecutor;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

/**
 * XxlJobProducer
 *
 * @author nayan
 * @date 2023/4/26 14:53
 */
@ApplicationScoped
public class XxlJobProducer {

    private XxlJobExecutor executor;

    @PreDestroy
    public void close() {
        if (Objects.nonNull(executor)) {
            executor.destroy();
        }
    }

    public void setExecutor(XxlJobExecutor executor) {
        this.executor = executor;
    }
}

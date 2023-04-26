package io.quarkiverse.xxl.job.runtime.executor;

import com.xxl.job.core.executor.XxlJobExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * XxlJobQuarkusExecutor
 *
 * @author nayan
 * @date 2023/4/26 14:47
 */

public class XxlJobQuarkusExecutor extends XxlJobExecutor {

    private List<XxlJobExecutorQuarkusJob> jobs = new ArrayList<>();

    @Override
    public void start() throws Exception {
        if (jobs == null || jobs.size() == 0) {
            return;
        }
        jobs.forEach(job -> registJobHandler(job.getAnnotation(), job.getBeanInstance(), job.getMethod()));
        super.start();
    }

    public List<XxlJobExecutorQuarkusJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<XxlJobExecutorQuarkusJob> jobs) {
        this.jobs = jobs;
    }
}

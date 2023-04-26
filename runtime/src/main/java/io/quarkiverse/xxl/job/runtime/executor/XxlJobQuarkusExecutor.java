package io.quarkiverse.xxl.job.runtime.executor;

import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.executor.XxlJobExecutor;

import java.io.File;
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

    private static void createLogDir() {
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        String jobLogFileName = xxlJobContext.getJobLogFileName();
        new File(jobLogFileName).getParentFile().mkdirs();
    }

    @Override
    public void start() throws Exception {
        if (jobs == null || jobs.size() == 0) {
            return;
        }
        jobs.forEach(job -> registJobHandler(job.getAnnotation(), job.getBeanInstance(), job.getMethod()));

        //        createLogDir();
        super.start();
    }

    public List<XxlJobExecutorQuarkusJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<XxlJobExecutorQuarkusJob> jobs) {
        this.jobs = jobs;
    }
}

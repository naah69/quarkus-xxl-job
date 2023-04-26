package io.quarkiverse.xxl.job.runtime.runtime;

import com.xxl.job.core.handler.annotation.XxlJob;
import io.quarkiverse.xxl.job.runtime.config.XxlJobConfig;
import io.quarkiverse.xxl.job.runtime.executor.XxlJobExecutorQuarkusJob;
import io.quarkiverse.xxl.job.runtime.executor.XxlJobQuarkusExecutor;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;

import java.lang.reflect.Method;
import java.util.List;

/**
 * XxlJobRecorder
 *
 * @author nayan
 * @date 2023/4/26 14:45
 */
@Recorder
public class XxlJobRecorder {
    public final XxlJobConfig config;

    public XxlJobRecorder(XxlJobConfig config) {
        this.config = config;
    }

    public void init(List<XxlJobExecutorQuarkusJob> jobs) throws Exception {
        if (config.enabled) {
            paddingJobInfo(jobs);
            XxlJobQuarkusExecutor executor = buildXxlJobExecutor(jobs);
            executor.start();
            Arc.container().instance(XxlJobProducer.class).get().setExecutor(executor);
        }

    }

    private static void paddingJobInfo(List<XxlJobExecutorQuarkusJob> jobs) {
        jobs.forEach(job -> {
            try {
                String methodName = job.getMethodName();
                Class instanceClass = job.getInstanceClass();

                Object instance = paddingJobInstance(job, instanceClass);
                paddingJobMethod(job, methodName, instance);
                paddingJobAnnotation(job, methodName, instanceClass);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void paddingJobMethod(XxlJobExecutorQuarkusJob job, String methodName, Object instance)
            throws NoSuchMethodException {
        Method method = instance.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        job.setMethod(method);
    }

    private static Object paddingJobInstance(XxlJobExecutorQuarkusJob job, Class instanceClass) {
        Object instance = Arc.container().instance(instanceClass).get();
        job.setBeanInstance(instance);
        return instance;
    }

    private static void paddingJobAnnotation(XxlJobExecutorQuarkusJob job, String methodName, Class instanceClass)
            throws NoSuchMethodException {
        Method method1 = instanceClass.getMethod(methodName);
        XxlJob annotation = method1.getAnnotation(XxlJob.class);
        job.setAnnotation(annotation);
    }

    private XxlJobQuarkusExecutor buildXxlJobExecutor(List<XxlJobExecutorQuarkusJob> jobs) {
        XxlJobQuarkusExecutor executor = new XxlJobQuarkusExecutor();
        executor.setJobs(jobs);
        config.admin.addresses.ifPresent(executor::setAdminAddresses);
        config.accessToken.ifPresent(executor::setAccessToken);
        XxlJobConfig.XxlJobExecutorConfig executorConfig = config.executor;
        String appname = executorConfig.appname;
        if (executorConfig.namespace.isPresent()) {
            appname = executorConfig.namespace.get() + "-" + appname;
        }
        executor.setAppname(appname);
        executorConfig.address.ifPresent(executor::setAddress);
        executorConfig.ip.ifPresent(executor::setIp);
        executor.setPort(executorConfig.port);
        executorConfig.logpath.ifPresent(executor::setLogPath);
        executorConfig.logretentiondays.ifPresent(executor::setLogRetentionDays);
        return executor;
    }

}
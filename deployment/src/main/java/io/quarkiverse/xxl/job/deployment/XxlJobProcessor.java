package io.quarkiverse.xxl.job.deployment;

import com.xxl.job.core.handler.annotation.XxlJob;
import io.quarkiverse.xxl.job.runtime.executor.XxlJobExecutorQuarkusJob;
import io.quarkiverse.xxl.job.runtime.runtime.XxlJobProducer;
import io.quarkiverse.xxl.job.runtime.runtime.XxlJobRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.jandex.*;

import java.util.LinkedList;
import java.util.List;

class XxlJobProcessor {

    private static final String FEATURE = "xxl-job";

    private static final DotName XXL_JOB = DotName.createSimple(XxlJob.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void addSnowflakeRegister(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemBuildProducer) {
        additionalBeanBuildItemBuildProducer.produce(AdditionalBeanBuildItem.unremovableOf(XxlJobProducer.class));
    }

    @BuildStep
    void xxlJobBuildItem(BuildProducer<XxlJobBuildItem> jobProducer, CombinedIndexBuildItem indexBuildItem,
            BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemBuildProducer) {
        for (AnnotationInstance instance : indexBuildItem.getIndex().getAnnotations(XXL_JOB)) {
            AnnotationTarget target = instance.target();
            if (target.kind() == AnnotationTarget.Kind.METHOD) {
                MethodInfo methodInfo = target.asMethod();
                ClassInfo classInfo = methodInfo.declaringClass();
                String className = classInfo.name().toString();
                String methodName = methodInfo.name().toString();
                jobProducer.produce(new XxlJobBuildItem(className, methodName));
                additionalBeanBuildItemBuildProducer.produce(AdditionalBeanBuildItem.unremovableOf(className));
            }
        }

    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    public void initial(XxlJobRecorder recorder, List<XxlJobBuildItem> xxlJobBuildItems) throws Exception {

        List<XxlJobExecutorQuarkusJob> items = new LinkedList<>();
        for (XxlJobBuildItem buildItem : xxlJobBuildItems) {
            items.add(new XxlJobExecutorQuarkusJob(buildItem.getClassName(), buildItem.getMethodName()));
        }
        recorder.init(items);
    }
}

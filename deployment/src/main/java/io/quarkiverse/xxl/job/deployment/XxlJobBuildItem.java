package io.quarkiverse.xxl.job.deployment;

import io.quarkus.builder.item.MultiBuildItem;

/**
 * XxlJobBuildItem
 *
 * @author nayan
 * @date 2023/4/26 15:29
 */
public final class XxlJobBuildItem extends MultiBuildItem {
    private final String className;
    private final String methodName;

    public XxlJobBuildItem(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

}

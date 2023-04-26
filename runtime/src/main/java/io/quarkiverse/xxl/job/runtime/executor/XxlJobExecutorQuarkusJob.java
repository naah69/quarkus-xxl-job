package io.quarkiverse.xxl.job.runtime.executor;

import com.xxl.job.core.handler.annotation.XxlJob;

import java.lang.reflect.Method;

/**
 * XxlJobExecutorItem
 *
 * @author nayan
 * @date 2023/4/26 15:49
 */
public class XxlJobExecutorQuarkusJob {
    private String className;
    private String methodName;
    private Method method;
    private Object beanInstance;
    private XxlJob annotation;

    public XxlJobExecutorQuarkusJob() {
    }

    public XxlJobExecutorQuarkusJob(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public Class getInstanceClass() {
        try {
            return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getBeanInstance() {
        return beanInstance;
    }

    public void setBeanInstance(Object beanInstance) {
        this.beanInstance = beanInstance;
    }

    public XxlJob getAnnotation() {
        return annotation;
    }

    public void setAnnotation(XxlJob annotation) {
        this.annotation = annotation;
    }
}

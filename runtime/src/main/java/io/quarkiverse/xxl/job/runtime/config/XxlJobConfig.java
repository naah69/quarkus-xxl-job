package io.quarkiverse.xxl.job.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;

/**
 * XxlJobConfig
 *
 * @author nayan
 * @date 2023/4/26 14:38
 */
@ConfigRoot(name = "xxl-job", phase = ConfigPhase.RUN_TIME)
public class XxlJobConfig {

    /**
     * 开关
     */
    @ConfigItem(defaultValue = "true")
    public boolean enabled;

    /**
     * 调度中心配置
     */
    @ConfigItem
    public XxlJobAdminConfig admin;

    /**
     * 执行器通讯TOKEN
     * [选填]：非空时启用；
     */
    public Optional<String> accessToken;

    /**
     * 执行器配置
     */
    @ConfigItem
    public XxlJobExecutorConfig executor;

    @ConfigGroup
    public static class XxlJobAdminConfig {
        /**
         * 调度中心部署根地址
         * [选填]：如调度中心集群部署存在多个地址则用逗号分隔。
         * 执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
         */
        public Optional<String> addresses;
    }

    @ConfigGroup
    public static class XxlJobExecutorConfig {

        /**
         * 执行器AppName
         * [选填]：执行器心跳注册分组依据；为空则关闭自动注册
         */
        @ConfigItem(defaultValue = "${quarkus.application.name}")
        public String appname;

        /**
         * 命名空间，
         * [选填]：如果有则会和appname组合成`namespace-appname`作为appname
         */
        public Optional<String> namespace;

        /**
         * 执行器注册
         * [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。
         * 从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        public Optional<String> address;

        /**
         * 执行器IP
         * [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，
         * 该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
         */
        public Optional<String> ip;

        /**
         * 执行器端口号
         * [选填]：小于等于0则自动获取；
         * 默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
         */
        @ConfigItem(defaultValue = "9999")
        public Integer port;

        /**
         * 控制台日志
         */
        @ConfigItem(defaultValue = "true")
        public boolean consolelog;

        /**
         * 执行器运行日志文件存储磁盘路径
         * [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
         */
        public Optional<String> logpath;

        /**
         * 执行器日志文件保存天数
         * [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        public Optional<Integer> logretentiondays;
    }

}

package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import lombok.var;

import java.util.List;
import java.util.function.IntFunction;

/**
 * 分组下机器地址相同，不同JOB均匀散列在不同机器上，保证分组下机器分配JOB平均；且每个JOB固定调度其中一台机器； a、virtual node：解决不均衡问题 b、hash method
 * replace hashCode：String的hashCode可能重复，需要进一步扩大hashCode的取值范围
 */
public class ExecutorRouteShardingBroadcast implements ExecutorRouter {

    @Override
    public InstanceInfo[] route(CoordinatorContext context, TriggerParam triggerParam, List<String> expression) {
        var list = context.getInstanceInfoList(triggerParam.getHandler(), expression);
        return list.toArray(new InstanceInfo[0]);
    }
}
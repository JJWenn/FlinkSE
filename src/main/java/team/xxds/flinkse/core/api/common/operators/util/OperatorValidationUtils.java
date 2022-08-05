package team.xxds.flinkse.core.api.common.operators.util;

import team.xxds.flinkse.core.api.common.ExecutionConfig;
import team.xxds.flinkse.core.util.Preconditions;

public class OperatorValidationUtils {
    private OperatorValidationUtils() {}

    // 默认算子可并行
    public static void validateParallelism(int parallelism) {
        validateParallelism(parallelism, true);
    }

    // 验证并行度
    public static void validateParallelism(int parallelism, boolean canBeParallel) {
        Preconditions.checkArgument(
                canBeParallel || parallelism == 1,
                "The parallelism of non parallel operator must be 1.");
        Preconditions.checkArgument(
                parallelism > 0 || parallelism == ExecutionConfig.PARALLELISM_DEFAULT,
                "The parallelism of an operator must be at least 1, or ExecutionConfig.PARALLELISM_DEFAULT (use system default).");
    }
}

package team.xxds.flinkse.stream.api.datastream;

import team.xxds.flinkse.core.api.common.operators.util.OperatorValidationUtils;
import team.xxds.flinkse.core.api.dag.Transformation;
import team.xxds.flinkse.stream.api.environment.StreamExecutionEnvironment;

public class SingleOutputStreamOperator<T> extends DataStream<T> {

    // 指定是否可算子并行（默认可并行）
    protected boolean nonParallel = false;

    protected SingleOutputStreamOperator(
            StreamExecutionEnvironment environment, Transformation<T> transformation) {
        super(environment, transformation);
    }

    public SingleOutputStreamOperator<T> setParallelism(int parallelism) {
        OperatorValidationUtils.validateParallelism(parallelism, canBeParallel());
        transformation.setParallelism(parallelism);

        return this;
    }

    private boolean canBeParallel() {
        return !nonParallel;
    }



}

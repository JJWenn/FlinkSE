package team.xxds.flinkse.stream.api.datastream;

import team.xxds.flinkse.core.api.common.ExecutionConfig;
import team.xxds.flinkse.core.api.dag.Transformation;
import team.xxds.flinkse.core.util.Preconditions;
import team.xxds.flinkse.stream.api.environment.StreamExecutionEnvironment;

public class DataStream<T> {

    protected final StreamExecutionEnvironment environment;

    protected final Transformation<T> transformation;

    public DataStream(StreamExecutionEnvironment environment, Transformation<T> transformation) {
        this.environment = Preconditions.checkNotNull(environment,
                "Execution Environment must not be null.");
        this.transformation = Preconditions.checkNotNull(transformation,
                "Stream Transformation must not be null.");
    }

    public int getId() {
        return transformation.getId();
    }

    public int getParallelism() {
        return transformation.getParallelism();
    }

    public StreamExecutionEnvironment getExecutionEnvironment() {
        return environment;
    }

    public ExecutionConfig getExecutionConfig() {
        return environment.getConfig();
    }

    // 将内部类指向外部类的引用设置为null，确保序列化过程的成功
    // Flink中算子都是通过序列化分发到各节点上，所以要确保算子对象是可以被序列化的。
    protected <F> F clean(F f) {
        return getExecutionEnvironment().clean(f);
    }






}

package team.xxds.flinkse.core.api.dag;

import team.xxds.flinkse.core.api.common.operators.util.OperatorValidationUtils;
import team.xxds.flinkse.core.api.common.typeinfo.TypeInformation;
import team.xxds.flinkse.core.util.Preconditions;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class Transformation<T> {

    // 使用AtomicInteger保证原子性
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    // 为每一个Transformation操作注册一个独一无二的id
    public static int getNewNodeId() {
        return ID_COUNTER.incrementAndGet();
    }

    protected final int id;
    protected String name;
    protected TypeInformation<T> outputType;
    private int parallelism;

    public Transformation(String name, TypeInformation<T> outputType, int parallelism) {
        this.id = getNewNodeId();
        // 检查空对象
        this.name = Preconditions.checkNotNull(name);
        this.outputType = outputType;
        this.parallelism = parallelism;

    }

    public int getId() {
        return id;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        OperatorValidationUtils.validateParallelism(parallelism);
        this.parallelism = parallelism;
    }

}

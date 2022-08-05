package team.xxds.flinkse.stream.api.environment;

import team.xxds.flinkse.core.api.common.ExecutionConfig;
import team.xxds.flinkse.core.api.common.typeinfo.TypeInformation;
import team.xxds.flinkse.core.api.dag.Transformation;
import team.xxds.flinkse.core.api.java.ClosureCleaner;
import team.xxds.flinkse.stream.api.datastream.DataStreamSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class StreamExecutionEnvironment {

    protected final ExecutionConfig config = new ExecutionConfig();

    protected final List<Transformation<?>> transformations = new ArrayList<>();

    public ExecutionConfig getConfig() {
        return config;
    }

    public <F> F clean(F f) {
        // 判断闭包清除器功能是否可用
        if (getConfig().isClosureCleanerEnabled()) {
            ClosureCleaner.clean(f, getConfig().getClosureCleanerLevel(), true);
        }
        // 确认对象可序列化
        ClosureCleaner.ensureSerializable(f);
        return f;
    }

    // 添加Source源算子
    public <OUT> DataStreamSource<OUT> addSource(SourceFunction<OUT> function) {
        return addSource(function, "Custom Source");
    }

    public <OUT> DataStreamSource<OUT> addSource(SourceFunction<OUT> function, String sourceName) {
        return addSource(function, sourceName, null);
    }

    public <OUT> DataStreamSource<OUT> addSource(
            SourceFunction<OUT> function, TypeInformation<OUT> typeInfo) {
        return addSource(function, "Custom Source", typeInfo);
    }

    public <OUT> DataStreamSource<OUT> addSource(
            SourceFunction<OUT> function, String sourceName, TypeInformation<OUT> typeInfo) {
        return addSource(function, sourceName, typeInfo, Boundedness.CONTINUOUS_UNBOUNDED);
    }

    private <OUT> DataStreamSource<OUT> addSource(
            final SourceFunction<OUT> function,
            final String sourceName,
            @Nullable final TypeInformation<OUT> typeInfo,
            final Boundedness boundedness) {
        checkNotNull(function);
        checkNotNull(sourceName);
        checkNotNull(boundedness);

        TypeInformation<OUT> resolvedTypeInfo =
                getTypeInfo(function, sourceName, SourceFunction.class, typeInfo);

        boolean isParallel = function instanceof ParallelSourceFunction;

        clean(function);

        final StreamSource<OUT, ?> sourceOperator = new StreamSource<>(function);
        return new DataStreamSource<>(
                this, resolvedTypeInfo, sourceOperator, isParallel, sourceName, boundedness);
    }


}

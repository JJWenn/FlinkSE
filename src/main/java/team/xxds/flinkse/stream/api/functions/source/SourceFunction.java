package team.xxds.flinkse.stream.api.functions.source;

import java.io.Serializable;

public interface SourceFunction<T> extends Serializable {

    void run(SourceContext<T> ctx) throws Exception;

    void cancel();

    interface SourceContext<T> {
        void collect(T element);

        void collectWithTimestamp(T element, long timestamp);

//        void emitWatermark(Watermark mark);

        void markAsTemporarilyIdle();

        Object getCheckpointLock();

        void close();

    }

}

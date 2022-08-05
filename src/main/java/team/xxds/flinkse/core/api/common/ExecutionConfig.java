package team.xxds.flinkse.core.api.common;

public class ExecutionConfig {

    // 默认并行度
    public static final int PARALLELISM_DEFAULT = -1;

    private ClosureCleanerLevel closureCleanerLevel = ClosureCleanerLevel.RECURSIVE;

    public boolean isClosureCleanerEnabled() {
        return !(closureCleanerLevel == ClosureCleanerLevel.NONE);
    }

    public enum ClosureCleanerLevel {
        // "Disables the closure cleaner completely."
        NONE,
        // "Cleans only the top-level class without recursing into fields."
        TOP_LEVEL,
        // "Cleans all fields recursively."
        RECURSIVE();
    }

    public ClosureCleanerLevel getClosureCleanerLevel() {
        return closureCleanerLevel;
    }

}

package services.dataCollectors.mergeToolExecutors


import java.nio.file.Path

class LastMergeMergeToolExecutorDataCollector extends BaseMergeToolExecutorDataCollector {

    private static final String DIFF3_BINARY_PATH = "/usr/bin/diff3"

    @Override
    protected List<String> getArgumentsForTool(Path file, Path outputFile) {
        return Arrays.asList(DIFF3_BINARY_PATH,
                "-m",
                file.resolve("left.java").toAbsolutePath().toString(),
                file.resolve("base.java").toAbsolutePath().toString(),
                file.resolve("right.java").toAbsolutePath().toString()
            )
    }

    @Override
    String getToolName() {
        return "diff3"
    }
}

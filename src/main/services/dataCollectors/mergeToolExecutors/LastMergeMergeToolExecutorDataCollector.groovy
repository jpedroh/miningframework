package services.dataCollectors.mergeToolExecutors


import java.nio.file.Path

import static app.MiningFramework.arguments

class LastMergeMergeToolExecutorDataCollector extends BaseMergeToolExecutorDataCollector {

    private static final String LAST_MERGE_BINARY_PATH = "${System.getProperty("user.dir")}/dependencies/last-merge"

    @Override
    protected List<String> getArgumentsForTool(Path file, Path outputFile) {
        return Arrays.asList(LAST_MERGE_BINARY_PATH,
                "merge",
                "--base-path=${file.resolve("base${arguments.getFileExtension()}").toAbsolutePath().toString()}".toString(),
                "--left-path=${file.resolve("left${arguments.getFileExtension()}").toAbsolutePath().toString()}".toString(),
                "--right-path=${file.resolve("right${arguments.getFileExtension()}").toAbsolutePath().toString()}".toString(),
                "--merge-path=${outputFile.toAbsolutePath().toString()}".toString()
        )
    }

    @Override
    String getToolName() {
        return "last_merge"
    }
}

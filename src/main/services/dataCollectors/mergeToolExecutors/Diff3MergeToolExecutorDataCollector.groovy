package services.dataCollectors.mergeToolExecutors


import java.nio.file.Path

import static app.MiningFramework.arguments

class Diff3MergeToolExecutorDataCollector extends BaseMergeToolExecutorDataCollector {
    @Override
    protected List<String> getArgumentsForTool(Path file, Path outputFile) {
        return Arrays.asList("sh",
                "./dependencies/diff3.sh",
                file.resolve("left${arguments.getFileExtension()}".toString()).toAbsolutePath().toString(),
                file.resolve("base${arguments.getFileExtension()}".toString()).toAbsolutePath().toString(),
                file.resolve("right${arguments.getFileExtension()}".toString()).toAbsolutePath().toString(),
                outputFile.toAbsolutePath().toString()
            )
    }

    @Override
    String getToolName() {
        return "diff3"
    }
}

package services.dataCollectors.mergeToolExecutors

import java.nio.file.Path

import static app.MiningFramework.arguments

class MergirafMergeToolExecutorDataCollector extends BaseMergeToolExecutorDataCollector {
    private static String MERGIRAF_PATH = "./dependencies/mergiraf"

    @Override
    protected List<String> getArgumentsForTool(Path file, Path outputFile) {
        return Arrays.asList(MERGIRAF_PATH,
                "merge",
                file.resolve("base${arguments.getFileExtension()}").toAbsolutePath().toString(),
                file.resolve("left${arguments.getFileExtension()}").toAbsolutePath().toString(),
                file.resolve("right${arguments.getFileExtension()}").toAbsolutePath().toString(),
                "--output=${outputFile.toAbsolutePath().toString()}".toString())
    }

    @Override
    String getToolName() {
        return "mergiraf"
    }
}

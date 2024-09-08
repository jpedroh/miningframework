package services.outputProcessors.genericMerge

import interfaces.OutputProcessor
import project.MergeCommit
import project.Project
import services.dataCollectors.GenericMerge.GenericMergeConfig
import services.dataCollectors.GenericMerge.model.MergeScenarioResult
import services.dataCollectors.S3MMergesCollector.MergeScenarioCollector
import services.mergeScenariosFilters.NonFastForwardMergeScenarioFilter

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class TriggerBuildAndTestsOutputProcessor implements OutputProcessor {
    @Override
    void processOutput() {
        Files.readAllLines(Paths.get(GenericMergeConfig.GENERIC_MERGE_REPORT_COMMITS_FILE_NAME))
                .stream()
                .map(line -> MergeScenarioLine.fromLine(line.split(",")))
                .filter(scenario -> !scenario.allFilesMatch && scenario.result != MergeScenarioResult.SUCCESS_WITHOUT_CONFLICTS)
                .forEach(scenario -> triggerBuildForScenario(scenario))
    }

    private static void triggerBuildForScenario(MergeScenarioLine scenario) {
        // Trigger a build for left
        BuildRequester.requestBuildForCommitSha(scenario.project, scenario.mergeCommit.getLeftSHA())
        // Trigger a build for right
        BuildRequester.requestBuildForCommitSha(scenario.project, scenario.mergeCommit.getRightSHA())
        // Trigger a build for merge
        def mergeFiles = MergeScenarioCollector
                .collectMergeScenarios(scenario.project, scenario.mergeCommit)
                .stream()
                .filter(NonFastForwardMergeScenarioFilter::isNonFastForwardMergeScenario)
                .collect(Collectors.toList())
        BuildRequester.requestBuildWithRevision(scenario.project, scenario.mergeCommit, mergeFiles, scenario.tool)
    }

    private static class MergeScenarioLine {
        final Project project
        final MergeCommit mergeCommit
        final MergeScenarioResult result
        final String tool
        final boolean allFilesMatch

        MergeScenarioLine(Project project, MergeCommit mergeCommit, MergeScenarioResult result, String tool, boolean allFilesMatch) {
            this.project = project
            this.mergeCommit = mergeCommit
            this.result = result
            this.tool = tool
            this.allFilesMatch = allFilesMatch
        }

        static fromLine(String[] line) {
            def project = new Project(line[0], line[1])
            def mergeCommit = new MergeCommit(line[2], new String[]{line[3], line[4]}, line[5])
            def result = MergeScenarioResult.from(line[7])
            def tool = line[6]
            def allFilesMatch = line[8] == "true"
            return new MergeScenarioLine(project, mergeCommit, result, tool, allFilesMatch)
        }
    }
}
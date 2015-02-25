package pipeTesting;

import core.Artifact_Manifest;
import core.Pipe;

import java.io.*;
import java.util.List;

/**
 * Created by nwilson on 2/24/15.
 */
public class TestRunner {

    private final List<Test> tests;
    private final Pipe actuals;
    private final Pipe baselines;
    private final Artifact_Manifest manifest;
    private final Appendable output;

    private TestRunner(List<Test> tests, Pipe actuals, Pipe baselines, Artifact_Manifest manifest, Appendable output) {
        this.tests = tests;
        this.actuals = actuals;
        this.baselines = baselines;
        this.manifest = manifest;
        this.output = output;
    }

    public static TestRunner FromTestFile(File testFile, File manifestFile, File actualsDirectory, File baselinesDirectory, Appendable output) {
        Artifact_Manifest manifest = Artifact_Manifest.FromFile(manifestFile);

        return new TestRunner(
                TestLoader.TestsFromFile(testFile, manifest),
                Pipe.FromDirectory(actualsDirectory, manifest),
                Pipe.FromDirectory(baselinesDirectory, manifest),
                manifest,
                output);
    }

    /**
     *
     * @return true if all tests pass, false otherwise
     */
    public boolean RunTests() {
        boolean status = true;
        for (Test test : this.tests)
            status &= RunTest(test);
        return status;
    }

    /**
     *
     * @return true if test passes, false otherwise
     */
    private boolean RunTest(Test test) {
        TestRunInstance instance = new TestRunInstance(test);
        return instance.RunTest();
    }

    private class TestRunInstance {
        private final Test test;
        private long startTime;
        private long endTime;

        public TestRunInstance(Test test) {
            this.test = test;
        }

        public boolean RunTest() {
            startTime = System.currentTimeMillis();
            WriteTestEnvelope();
            boolean passed = true;
            try {
                test.Verify(actuals.ArtifactFromID(test.aid), baselines.ArtifactFromID(test.aid));
            } catch (Exception ex) {
                passed = false;
                for (StackTraceElement frame : ex.getStackTrace())
                    PrintlnToOutput(frame.toString());
                PrintlnToOutput(ex.getMessage());
            } finally {
                endTime = System.currentTimeMillis();
            }

            if (passed)
                WriteTestSuccess();
            else
                WriteTestFailure(null);

            PrintlnToOutput("");
            return passed;
        }

        private void WriteTestEnvelope() {
            // TODO
            PrintlnToOutput(String.format("%tT Starting test %s on artifact %s", startTime, test.name, test.aid.name));
        }

        private void WriteTestSuccess() {
            // TODO
            PrintlnToOutput(String.format("SUCCESS! in %d ms.", endTime - startTime));
        }

        private void WriteTestFailure(String msg) {
            // TODO
            PrintlnToOutput("FAILURE!!!");
        }
    }

    private void PrintlnToOutput(String string) {
        try {
            output.append(string);
            output.append(System.lineSeparator());
        } catch (IOException ex) {
            // TODO who cares
        }
    }
}

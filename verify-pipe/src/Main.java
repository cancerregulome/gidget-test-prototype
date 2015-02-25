import pipeTesting.TestRunner;

import java.io.File;

/**
 * Created by Nick on 2/20/2015.
 */
public class Main {

    private static final String usage =
            "Usage: verify-pipe.jar <manifest file> <pipeline dir> <baseline dir> <test-file>";

    public static void main(String[] args) {

        if (args.length != 4) {
            System.err.println(usage);
            System.exit(-1);
        }

        File manifestFile = FileFromArgOrExit(args[0], "artifact manifest file");
        File pipelineDir  = FileFromArgOrExit(args[1], "pipeline directory");
        File baselineDir  = FileFromArgOrExit(args[2], "baseline directory");
        File testsFile    = FileFromArgOrExit(args[3], "tests file");

        TestRunner testRunner = TestRunner.FromTestFile(testsFile, manifestFile, pipelineDir, baselineDir, System.out);

        boolean passed = testRunner.RunTests();

        if (passed)
            System.exit(0);
        else
            System.exit(1);
    }

    private static File FileFromArgOrExit(String arg, String prettyName) {
        File file = new File(arg);
        if (! file.exists()) {
            System.err.println(String.format("Could not find %s at %s!", prettyName, arg));
            System.exit(-2);
        }
        return file;
    }
}

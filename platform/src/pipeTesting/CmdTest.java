package pipeTesting;

import core.Artifact;
import core.ArtifactID;
import util.Os;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by nwilson on 2/23/15.
 */
public class CmdTest extends Test {

    private static final String BASELINE_ARG = "$BASELINE";
    private static final String ACTUAL_ARG   = "$ACTUAL";

    private final String cmd;

    public CmdTest(String cmd, ArtifactID aid, String name) {
        super(aid, name);
        this.cmd = cmd;
    }

    @Override
    void Verify(Artifact base, Artifact actual) {

        StringTokenizer tokenizer = new StringTokenizer(cmd, " ");
        List<String> cmdAndArgs = new ArrayList<>(3);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            switch (token) {
                case BASELINE_ARG:
                    token = base.file.getAbsolutePath();
                    break;
                case ACTUAL_ARG:
                    token = actual.file.getAbsolutePath();
                    break;
            }
            cmdAndArgs.add(token);
        }

        int status = Os.current.Exec(cmdAndArgs);
        System.out.println(status);
        assert status == 0;
    }
}

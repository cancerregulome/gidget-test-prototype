package pipeTesting;

import core.Artifact;
import core.ArtifactID;
import util.Os;

/**
 * Created by nwilson on 2/23/15.
 */
public class CmdTest extends Test {

    private static final String BASELINE_ARG = "\\$BASELINE";
    private static final String ACTUAL_ARG   = "\\$ACTUAL";

    private final String cmd;

    public CmdTest(String cmd, ArtifactID aid, String name) {
        super(aid, name);
        this.cmd = cmd;
    }

    @Override
    void Verify(Artifact base, Artifact actual) {
        String cmdParsed = this.cmd;
        cmdParsed = cmdParsed.replaceAll(BASELINE_ARG, base.file.getAbsolutePath());
        cmdParsed = cmdParsed.replaceAll(ACTUAL_ARG, actual.file.getAbsolutePath());
        assert Os.current.Exec(cmdParsed) == 0;
    }
}

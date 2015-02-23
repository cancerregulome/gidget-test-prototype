package pipeTesting;

/**
 * Created by nwilson on 2/23/15.
 */

import core.Artifact;
import core.Artifact_ID;

/**
 * Represents a test that is an error or was loaded incorrectly
 */
public class ErrorTest extends Test {

    private final String msg;

    public ErrorTest(String msg, String name, Artifact_ID aid) {
        super(aid, name);
        this.msg = msg;
    }

    @Override
    void Verify(Artifact base, Artifact actual) {
        assert false;
    }
}

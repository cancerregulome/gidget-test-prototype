package pipeTesting;

import core.Artifact;
import core.Artifact_ID;

/**
 * Created by nwilson on 2/19/15.
 */

public abstract class Test {

    public final Artifact_ID aid;
    public final String name;

    protected Test(Artifact_ID aid, String name) {
        this.aid = aid;
        this.name = name;
    }

    /**
     * Throws AssertionError is verification fails.
     * @param base
     * @param actual
     */
    abstract void Verify(Artifact base, Artifact actual); // TODO or something
}

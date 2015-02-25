package pipeTesting;

import core.Artifact;
import core.ArtifactID;

/**
 * Created by nwilson on 2/19/15.
 */

public abstract class Test {

    public final ArtifactID aid;
    public final String name;

    protected Test(ArtifactID aid, String name) {
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

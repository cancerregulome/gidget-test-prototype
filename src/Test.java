/**
 * Created by nwilson on 2/19/15.
 */

public abstract class Test {

    public final Artifact_ID aid;

    private Test(Artifact_ID aid) {
        this.aid = aid;
    }

    abstract void Verify(Artifact base, Artifact actual); // TODO or something
}

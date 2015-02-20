package core;

import java.io.File;

/**
 * Created by nwilson on 2/19/15.
 */

/**
 *  Represent an input or product of a pipeline. Has an Artifact_ID that identifies which artifact of the pipe this is.
 */
public class Artifact {

    public final Artifact_ID aid;
    public final File file;

    public Artifact(Artifact_ID aid, File file) {
        this.aid = aid;
        this.file = file;
    }
}
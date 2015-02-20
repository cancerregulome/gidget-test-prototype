/**
 * Created by nwilson on 2/19/15.
 */

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;

/**
 * Represents a completed pipeline with all of it's artifacts
 *
 * TODO make this an abstract class with two subclasses (baselines and real pipe)?
 */
public class Pipe {

    // an inversion of the artifact->aid mapping that's in the artifact itself
    private final Map<Artifact_ID, Artifact> mpaid_artifact;

    private Pipe(Iterable<Artifact> artifacts) {
        this.mpaid_artifact = new HashMap<Artifact_ID, Artifact>();

        for (Artifact artifact : artifacts) {
            mpaid_artifact.put(artifact.aid, artifact);
        }
    }

    /**
     * Creates a pipe by recursively scanning the artifact metadata in the directory rooted at the directory argument.
     *
     * @param directory: root of the file hierarchy
     * @return a Pipe
     */
    public static Pipe FromDirectory(File directory) {
        // TODO implement
        throw new NotImplementedException();
    }
}

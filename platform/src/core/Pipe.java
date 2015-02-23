package core;

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

    // name -> aid lookup
    private final Map<String, Artifact_ID> mpname_artifactID;
    private static final Artifact_ID aidAmbiguous = new Artifact_ID(null, null, null);

    private final Map<UUID, Artifact_ID> mpuuid_artifactID;

    private Pipe(Iterable<Artifact> artifacts) {
        this.mpaid_artifact = new HashMap<Artifact_ID, Artifact>();
        this.mpname_artifactID = new HashMap<String, Artifact_ID>();
        this.mpuuid_artifactID = new HashMap<UUID, Artifact_ID>();

        for (Artifact artifact : artifacts) {
            mpaid_artifact.put(artifact.aid, artifact);
            if (mpname_artifactID.containsKey(artifact.aid.name)) {
                mpname_artifactID.put(artifact.aid.name, aidAmbiguous);
            }
            else {
                mpname_artifactID.put(artifact.aid.name, artifact.aid);
            }
            // TODO check for duplicates and throw if there are any
            mpuuid_artifactID.put(artifact.aid.uuid, artifact.aid);
        }
    }

    public Artifact_ID ArtifactIDFromName(String name) {
        Artifact_ID aid = mpname_artifactID.get(name);
        if (aid == aidAmbiguous) {
            System.err.println("Artifact name " + name + " is ambiguous. Please refer to artifact by UUID instead");
            return null;
        }
        return aid;
    }

    public Artifact_ID ArtifactIDFromUUID(UUID uuid) {
        return mpuuid_artifactID.get(uuid);
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

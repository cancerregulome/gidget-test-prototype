package core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nwilson on 2/23/15.
 */
public class Artifact_Manifest {
    // name -> aid lookup
    private final Map<String, Artifact_ID> mpname_artifactID;
    private static final Artifact_ID aidAmbiguous = new Artifact_ID(null, null, null);

    private final Map<UUID, Artifact_ID> mpuuid_artifactID;

    private Artifact_Manifest(Iterable<Artifact_ID> ids) {
        this.mpname_artifactID = new HashMap<String, Artifact_ID>();
        this.mpuuid_artifactID = new HashMap<UUID, Artifact_ID>();

        for (Artifact_ID aid : ids) {
            AddArtifactID(aid);
        }
    }

    public void AddArtifactID(Artifact_ID aid) {
        if (mpname_artifactID.containsKey(aid.name)) {
            mpname_artifactID.put(aid.name, aidAmbiguous);
        }
        else {
            mpname_artifactID.put(aid.name, aid);
        }
        // TODO check for duplicates and throw if there are any
        mpuuid_artifactID.put(aid.uuid, aid);
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
}

package core;

import java.util.UUID;

/**
 * Created by nwilson on 2/19/15.
 */

/**
 * A unique identifier for a particular artifact (input or output) of a pipeline.
 *
 * TODO: Make this hierarchical and/or parameterized?
 */
public class ArtifactID {
    public final UUID uuid;
    public final String name;
    public final String description;

    public ArtifactID(UUID uuid, String name, String description) {
        this.uuid = uuid;
        if (name == "")
            name = null;
        this.name = name;
        this.description = description;
    }

    private ArtifactID(UUID uuid, String name) { this(uuid, name, null); }

    public static ArtifactID New(String name, String description) {
        return new ArtifactID(UUID.randomUUID(), name, description);
    }

    public static ArtifactID New(String name) {
        return New(name, null);
    }

    public String toString() {
        return String.format("Artifact_ID [name: %s uuid: %s desc: %s ]", name, uuid, description);
    }
}

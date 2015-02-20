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
public class Artifact_ID {
    public final UUID uuid;
    public final String name;
    public final String description;

    public Artifact_ID(UUID uuid, String name, String description) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }

    private Artifact_ID(UUID uuid, String name) { this(uuid, name, null); }

    public static Artifact_ID New(String name, String description) {
        return new Artifact_ID(UUID.randomUUID(), name, description);
    }

    public static Artifact_ID New(String name) {
        return New(name, null);
    }

    public String toString() {
        return name;
    }
}

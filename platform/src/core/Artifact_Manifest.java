package core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nwilson on 2/23/15.
 */
public class Artifact_Manifest {

    private static final CSVFormat MANIFEST_FORMAT = CSVFormat.newFormat('\t').withHeader("uuid", "name", "description");

    private static final Artifact_ID aidAmbiguous = new Artifact_ID(null, null, null);

    // aid inversions
    private final Map<String, Artifact_ID> mpname_artifactID;
    private final Map<UUID, Artifact_ID> mpuuid_artifactID;

    private Artifact_Manifest() {
        this.mpname_artifactID = new HashMap<String, Artifact_ID>();
        this.mpuuid_artifactID = new HashMap<UUID, Artifact_ID>();
    }

    private Artifact_Manifest(Iterable<Artifact_ID> ids) {
        this();
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

    public static Artifact_Manifest FromFile(File file) {
        try (Reader fileReader = new FileReader(file)) {
            CSVParser csvParser = new CSVParser(fileReader, MANIFEST_FORMAT);
            Artifact_Manifest manifest = new Artifact_Manifest();

            for (CSVRecord record : csvParser) {
                String name = record.get("name");
                UUID uuid = UUID.fromString(record.get("uuid"));
                String description = record.get("description");
                manifest.AddArtifactID(new Artifact_ID(uuid, name, description));
            }
            return manifest;

        } catch (IOException ex) {
            System.err.println("Could not read file " + file.getAbsolutePath());
            return null;
        }
    }

    public static void WriteToFile(File file) {
        throw new NotImplementedException(); // TODO
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Artifact_ID aid : mpuuid_artifactID.values()) {
            sb.append(aid.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    public Artifact_ID ArtifactIDFromUUID(UUID uuid) {
        return mpuuid_artifactID.get(uuid);
    }
}

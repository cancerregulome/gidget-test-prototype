package core;

import org.apache.commons.csv.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nwilson on 2/23/15.
 */
public class ArtifactManifest {

    private static final CSVFormat MANIFEST_FORMAT = CSVFormat.DEFAULT
            .withDelimiter('\t')
            .withHeader("uuid", "name", "description")
            .withIgnoreEmptyLines()
            .withSkipHeaderRecord()
            .withRecordSeparator('\n');

    private static final ArtifactID aidAmbiguous = new ArtifactID(null, null, null);

    // aid inversions
    private final Map<String, ArtifactID> mpname_artifactID;
    private final Map<UUID, ArtifactID> mpuuid_artifactID;

    private ArtifactManifest() {
        this.mpname_artifactID = new HashMap<String, ArtifactID>();
        this.mpuuid_artifactID = new HashMap<UUID, ArtifactID>();
    }

    private ArtifactManifest(Iterable<ArtifactID> ids) {
        this();
        for (ArtifactID aid : ids) {
            AddArtifactID(aid);
        }
    }

    public void AddArtifactID(ArtifactID aid) {
        if (mpname_artifactID.containsKey(aid.name)) {
            mpname_artifactID.put(aid.name, aidAmbiguous);
        }
        else {
            mpname_artifactID.put(aid.name, aid);
        }
        // TODO check for duplicates and throw if there are any
        mpuuid_artifactID.put(aid.uuid, aid);
    }

    public ArtifactID ArtifactIDFromName(String name) {
        ArtifactID aid = mpname_artifactID.get(name);
        if (aid == aidAmbiguous) {
            System.err.println("Artifact name " + name + " is ambiguous. Please refer to artifact by UUID instead");
            return null;
        }
        return aid;
    }

    public static ArtifactManifest FromFile(File file) {
        try (CSVParser in = new CSVParser(new FileReader(file), MANIFEST_FORMAT)) {
            ArtifactManifest manifest = new ArtifactManifest();
            for (CSVRecord record : in) {
                String name = record.get("name");
                UUID uuid = UUID.fromString(record.get("uuid"));
                String description = record.get("description");
                manifest.AddArtifactID(new ArtifactID(uuid, name, description));
            }
            return manifest;

        } catch (IOException ex) {
            System.err.println("Could not read file " + file.getAbsolutePath());
            return null;
        }
    }

    public void WriteToFile(File file) {
        try (CSVPrinter out = new CSVPrinter(new FileWriter(file), MANIFEST_FORMAT)) {
            for (ArtifactID aid : mpuuid_artifactID.values()) {
                out.printRecord(aid.uuid, aid.name, aid.description);
            }

        } catch (IOException ex) {
            System.err.println("Could not write to file " + file.getAbsolutePath());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArtifactID aid : mpuuid_artifactID.values()) {
            sb.append(aid.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    public ArtifactID ArtifactIDFromUUID(UUID uuid) {
        return mpuuid_artifactID.get(uuid);
    }
}

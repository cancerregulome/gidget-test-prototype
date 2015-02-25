package core;

/**
 * Created by nwilson on 2/19/15.
 */

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Represents a completed pipeline with all of it's artifacts
 *
 * TODO make this an abstract class with two subclasses (baselines and real pipe)?
 */
public class Pipe {

    private static final int UUID_INDEX = 0;
    private static final int PATH_INDEX = 1;

    private static final CSVFormat ARTIFACTS_FORMAT = CSVFormat.DEFAULT
            .withDelimiter('\t')
            .withIgnoreEmptyLines()
            .withRecordSeparator('\n')
            .withHeaderComments("uuid", "relative-path");

    // an inversion of the artifact->aid mapping that's in the artifact itself
    private final Map<ArtifactID, Artifact> mpaid_artifact;
    public final ArtifactManifest artifactManifest;

    private Pipe(Map<ArtifactID, Artifact> mpaid_artifact, ArtifactManifest artifactManifest) {
        this.mpaid_artifact = mpaid_artifact;
        this.artifactManifest = artifactManifest;
    }

    public Artifact ArtifactFromID(ArtifactID artifactID) {
        return mpaid_artifact.get(artifactID);
    }

    /**
     * Creates a pipe by recursively scanning the artifact metadata stored in extended file attributes in the direcrory
     * rooted at the directory argument.
     * @param directory
     * @param manifest
     * @return
     */
    public static Pipe FromDirectoryXattr(File directory, ArtifactManifest manifest) {
        // TODO implement
        throw new NotImplementedException();
    }

    /**
     * Creates a pipe by recursively scanning the artifact metadata stored in .artifact files in the directory rooted at
     * the directory argument.
     *
     * @param directory: root of the file hierarchy
     * @param manifest
     * @return a Pipe
     */
    public static Pipe FromDirectory(File directory, ArtifactManifest manifest) {
        Map<ArtifactID, Artifact> mpaid_artifact = new HashMap<>();
        FromDirectory_helper(mpaid_artifact, directory, manifest);
        return new Pipe(mpaid_artifact, manifest);
    }

    // recursively walk directory structure to look for artifacts
    private static void FromDirectory_helper(
            Map<ArtifactID, Artifact> accumulate,
            File file,
            ArtifactManifest manifest) {
        if (file.isDirectory()) {
            for (File subfile : file.listFiles()) {
                FromDirectory_helper(accumulate, subfile, manifest);
            }
        } else if (file.getName().equals(".artifacts")) {
            LoadArtifactsFile(file, accumulate, manifest);
        }
        // else do nothing
    }

    private static void LoadArtifactsFile(File artifactsFile, Map<ArtifactID, Artifact> accumulate, ArtifactManifest manifest) {
        try (CSVParser in = new CSVParser(new FileReader(artifactsFile), ARTIFACTS_FORMAT)) {
            for (CSVRecord record : in) {
                UUID uuid = UUID.fromString(record.get(UUID_INDEX));
                File artifactFile = SearchForArtifact(record.get(PATH_INDEX), artifactsFile.getParentFile());
                ArtifactID aid = manifest.ArtifactIDFromUUID(uuid);
                accumulate.put(aid, new Artifact(aid, artifactFile));
            }
        } catch (IOException ex) {
            System.err.println("Could not read file " + artifactsFile.getAbsolutePath());
        }
    }

    // Search for artifacts
    private static File SearchForArtifact(String relativePath, File directory) {

        // look up as relative path
        File artifactFile = new File(directory, relativePath);
        if (artifactFile.exists())
            return artifactFile;

        // check to make sure it's not an absolute path
        artifactFile = new File(relativePath);
        if (artifactFile.exists())
            return artifactFile;

        // TODO do some other smart things
        System.err.println(String.format("Could not find artifact file %s", relativePath));
        return null;
    }
}

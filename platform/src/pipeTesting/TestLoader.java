package pipeTesting;

import core.ArtifactID;
import core.ArtifactManifest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.*;

/**
 * Created by nwilson on 2/23/15.
 */
public class TestLoader {

    private static final String KEY_AID_NAME = "artifact_name";
    private static final String KEY_AID_UUID = "artifact_uuid";
    private static final String KEY_TESTS    = "tests";
    private static final String KEY_NAME     = "name";
    private static final String KEY_CMD      = "cmd";

    public static List<Test> TestsFromFile(File file, ArtifactManifest manifest) {
        List<Test> output = new LinkedList<>();

        try (FileInputStream fileIn = new FileInputStream(file)) {
            JSONObject object = new JSONObject(new JSONTokener(fileIn));
            JSONArray tests = object.getJSONArray(KEY_TESTS);
            for (int itest = 0; itest < tests.length(); itest++) {
                JSONObject test = tests.getJSONObject(itest);

                String name;
                if (test.has(KEY_NAME)) {
                    name = test.getString(KEY_NAME);
                } else{
                    name = null;
                }

                ArtifactID aid;
                if (test.has(KEY_AID_NAME)) {
                    aid = manifest.ArtifactIDFromName(test.getString(KEY_AID_NAME));
                } else if (test.has(KEY_AID_UUID)) {
                    aid = manifest.ArtifactIDFromUUID(UUID.fromString(test.getString(KEY_AID_UUID)));
                } else {
                    output.add(new ErrorTest("Must specify either artifact name or uuid.", name, null));
                    continue;
                }

                if (test.has(KEY_CMD)) {
                    output.add(new CmdTest(test.getString(KEY_CMD), aid, name));

                // } else if { TODO other kinds of tests
                } else {
                    output.add(new ErrorTest("Must specify a command for the test", name, aid));
                }
            }

        }
        catch (FileNotFoundException ex) {
            System.err.println("Could not find file " + file.toString());
            return null;
        }
        catch (IOException ex) {
            System.err.println("Could not read file " + file.toString());
            return null;
        }

        return output;
    }
}

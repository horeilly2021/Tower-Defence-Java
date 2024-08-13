package WizardTD;

import processing.core.PApplet;
import processing.data.JSONObject;

public class JsonFileReader {
    private PApplet parent;

    public JsonFileReader(PApplet parent) {
        this.parent = parent;
    }

    /**
     * Reads a JSON file and returns its content as a JSONObject.
     *
     * @param filePath The path to the JSON file to be read.
     * @return A JSONObject containing the parsed content of the JSON file.
     */
    public JSONObject readJsonFile(String filePath) {
        JSONObject jsonObject = null;
        try {
            jsonObject = parent.loadJSONObject(filePath);
        } catch (Exception e) {
            System.err.println("JSON file error. File not found");
            System.exit(1);
        }
        return jsonObject;
    }
}

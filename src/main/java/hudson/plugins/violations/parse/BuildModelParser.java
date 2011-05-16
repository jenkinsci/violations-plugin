package hudson.plugins.violations.parse;

import hudson.plugins.violations.model.BuildModel;
import hudson.plugins.violations.model.Severity;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Parses a violatios build xml report file.
 */
public class BuildModelParser extends AbstractParser {

    private BuildModel buildModel;

    /**
     * Fluid setting of buildModel attribute.
     * @param buildModel the model to populate.
     * @return this object.
     */
    public BuildModelParser buildModel(BuildModel buildModel) {
        this.buildModel = buildModel;
        return this;
    }

    /**
     * Parse the build model file.
     * @throws IOException if there is an I/O problem.
     * @throws XmlPullParserException if there is a parsing problem.
     */
    protected void execute()
        throws IOException, XmlPullParserException {

        // Ensure that the top level tag is "violations"
        expectNextTag("violations");
        getParser().next(); // consume the "violations" tag
        // loop tru the child elements, getting the "file" ones
        while (skipToTag("type")) {
            parseTypeElement();
        }
    }

    private void parseTypeElement()
        throws IOException, XmlPullParserException {

        String type = checkNotBlank("name");
        getParser().next();  // consume "file" tag
        buildModel.getFileCounts(type);

        while (skipToTag("file")) {
            String filename = checkNotBlank("name");
            getParser().next();
            int[] counts = new int[Severity.NUMBER_SEVERITIES];
            while (skipToTag("severity")) {
                counts[getInt("level")] = getInt("count");
                skipTag();
            }
            buildModel.addFileCount(
                type, filename, counts);
            endElement();
        }
        endElement();
    }
}

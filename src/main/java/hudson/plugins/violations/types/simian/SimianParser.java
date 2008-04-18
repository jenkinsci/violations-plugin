package hudson.plugins.violations.types.simian;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.util.StringUtil;

/**
 * Parser for Simian XML files.
 * 
 * Simian (Similarity Analyser) identifies duplication in any text files.
 * @see http://www.redhillconsulting.com.au/products/simian/ 
 */
public class SimianParser extends AbstractTypeParser {
    
    class DuplicationBlock {
        String absolutePath;
        String fileName;
        int startLineNumber;
        int endLineNumber;
    }
    
    static final String TYPE_NAME = "simian";
    private static final int LOW_LIMIT = 10;
    private static final int MEDIUM_LIMIT = 50;

    @Override
    protected void execute() throws IOException, XmlPullParserException {
        expectNextTag("simian");
        getParser().next(); 

        while (skipToTag("check")) {
            getParser().next(); 
            while (skipToTag("set")) {
                getParser().next();
                parseSetElement();
                endElement();
            }
            endElement();
        }
        
        endElement();
    }

    /**
     * Parses the <set/> block and adds the found duplication violations.
     * @throws IOException thrown by XmlPullParser
     * @throws XmlPullParserException thrown by XmlPullParser
     */
    private void parseSetElement() throws IOException, XmlPullParserException {
        Set<DuplicationBlock> blocks = new HashSet<DuplicationBlock>();
        while (skipToTag("block")) {
            blocks.add(parseBlockElement());
            getParser().next();
            endElement();
        }
        
        for (DuplicationBlock block : blocks) {
            Set<DuplicationBlock> otherBlocks = new HashSet<DuplicationBlock>(blocks);
            otherBlocks.remove(block);
                        
            Violation violation = createViolation(block);
            setViolationMessages(violation, block, otherBlocks);
            
            FullFileModel fileModel = getFileModel(block.absolutePath);
            fileModel.addViolation(violation);
        }
    }

    /**
     * Parses the current <block/> and returns a DuplicationBlock.
     * @return a DuplicationBlock representing the <block/>
     * @throws XmlPullParserException thrown by XmlPullParser
     * @throws IOException thrown by XmlPullParser
     */
    private DuplicationBlock parseBlockElement() throws XmlPullParserException, IOException {
        DuplicationBlock block = new DuplicationBlock(); 
        block.absolutePath = fixAbsolutePath(checkNotBlank("sourceFile")).replace('\\', '/');
        block.fileName = new File(block.absolutePath).getName();
        block.startLineNumber = Integer.parseInt(getString("startLineNumber"));
        block.endLineNumber = Integer.parseInt(getString("endLineNumber"));
        return block;
    }

    /**
     * Sets the message and popupMessage on the violation and using the other blocks as references.
     * The popupMessage will set to something along the line 
     * "Duplication of 20 lines from line 23, line 45 in ClassFoo.java".
     * The message will be set to something along the line 
     * "Duplication of 20 lines from <a href=''>line 23</a>, <a href=''>line 45 in ClassFoo.java</a>".
     * @param violation the violation to update the messages on
     * @param block the current block that is referencing the other blocks
     * @param otherBlocks other blocks that are used for references in the messages.
     */
    private void setViolationMessages(Violation violation, DuplicationBlock block, Set<DuplicationBlock> otherBlocks) {
        StringBuilder popupMessage = new StringBuilder("Duplication of ");
        popupMessage.append(block.endLineNumber - block.startLineNumber + 1);
        popupMessage.append(" lines from ");
        StringBuilder message = new StringBuilder(popupMessage);
        
        int i = 0;
        int size = otherBlocks.size();        
        for (Iterator<DuplicationBlock> iterator = otherBlocks.iterator(); iterator.hasNext();) {
            DuplicationBlock otherViolation = iterator.next();
            String hrefString = getOtherHrefString(block, otherViolation);
            String duplicationString = getOtherDuplicationString(block, otherViolation);
            message.append("<a href='");
            message.append(hrefString);
            message.append("'>");
            message.append(duplicationString);
            message.append("</a>");
            popupMessage.append(duplicationString);
            
            String postfix;
            i++;
            if (i < (size - 1)) {
                postfix = ", ";
            } else if (i == (size - 1)){ 
                postfix = " and ";
            } else {
                postfix = ".";
            }
            popupMessage.append(postfix);
            message.append(postfix);
        }
        violation.setPopupMessage(popupMessage.toString());
        violation.setMessage(message.toString());
    }
    
    /**
     * Returns a href for the blocks.
     * @param self the current block.
     * @param other the other block that the href is returned for.
     * @return a string containing the href for the other block file.
     */
    private String getOtherHrefString(DuplicationBlock self, DuplicationBlock other) {
        String path = StringUtil.relativePath(self.absolutePath + "/bats", other.absolutePath);
        return path + "#line" + other.startLineNumber;
    }
    
    /**
     * Returns a string describing the duplication with line numbers.
     * @param self the current block
     * @param other the other block that should be described
     * @return a string describing the duplication with line numbers.
     */
    private String getOtherDuplicationString(DuplicationBlock self, DuplicationBlock other) {
        StringBuilder builder = new StringBuilder();
        builder.append("line ");
        builder.append(other.startLineNumber);
        if (! self.absolutePath.equals(other.absolutePath)) {
            builder.append(" in ");
            builder.append(other.fileName);
        }
        return builder.toString();
    }

    /**
     * Create a violation from the duplication block
     * @param block the block to create a violation from
     * @return a violation
     */
    private Violation createViolation(DuplicationBlock block) {
        Violation violation = new Violation();
        violation.setType(TYPE_NAME);
        violation.setSource("duplication");
        int startLineNumber = block.startLineNumber;
        int endLineNumber = block.endLineNumber;
        int lineCount = endLineNumber - startLineNumber + 1;
        violation.setLine(startLineNumber);
        setViolationSeverity( violation,
              (lineCount < LOW_LIMIT) ? Severity.LOW
              : (lineCount < MEDIUM_LIMIT) ? Severity.MEDIUM
              : Severity.HIGH);
        return violation;
    }
    
    /**
     * Sets violation severity.
     * @param v violation to update.
     * @param severity the severity text.
     */
    private void setViolationSeverity(Violation v, String severity) {
        v.setSeverity(severity);
        v.setSeverityLevel(Severity.getSeverityLevel(severity));
    }
}

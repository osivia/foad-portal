package fr.gouv.education.foad.taglib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimeType;
import javax.servlet.jsp.JspException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.taglib.common.PortalSimpleTag;

/**
 * Get MIME type icon tag.
 * 
 * @author Cédric Krommenhoek
 * @see PortalSimpleTag
 */
public class GetMimeTypeIconTag extends PortalSimpleTag {

    /** HTML class prefix. */
    private static final String HTML_CLASS_PREFIX = "mime-type mime-type-";


    /** MIME types. */
    private static Map<String, MimeTypeValue> mimeTypes;


    /** MIME type string representation. */
    private String mimeType;


    /**
     * Constructor.
     */
    public GetMimeTypeIconTag() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void doTag() throws JspException, IOException {
        if (mimeTypes == null) {
            mimeTypesInitialization();
        }

        // MIME type value
        MimeTypeValue value = mimeTypes.get(this.mimeType);
        MimeType mimeType;
        String extension;
        String description;
        if (value == null) {
            mimeType = null;
            extension = null;
            description = null;
        } else {
            mimeType = value.getMimeType();
            extension = value.getExtension();
            description = value.getDescription();
        }


        // Icon
        Element icon = DOM4JUtils.generateElement("i", HTML_CLASS_PREFIX + StringUtils.defaultIfEmpty(extension, "unknown"), StringUtils.EMPTY);
        if (mimeType != null) {
            String primaryType = mimeType.getPrimaryType();
            String subType = mimeType.getSubType();
            if (StringUtils.isNotEmpty(primaryType)) {
                DOM4JUtils.addDataAttribute(icon, "primary-type", primaryType);
            }
            if (StringUtils.isNotEmpty(subType)) {
                DOM4JUtils.addDataAttribute(icon, "sub-type", subType);
            }
        }
        if (StringUtils.isNotEmpty(extension)) {
            DOM4JUtils.addDataAttribute(icon, "extension", extension);
        }
        if (StringUtils.isNotEmpty(description)) {
            DOM4JUtils.addDataAttribute(icon, "description", description);
        }

        // HTML writer
        HTMLWriter htmlWriter = new HTMLWriter(this.getJspContext().getOut());
        htmlWriter.setEscapeText(false);
        htmlWriter.write(icon);
    }


    /**
     * MIME types initialization.
     * 
     * @throws IOException
     */
    private static synchronized void mimeTypesInitialization() throws IOException {
        if (mimeTypes == null) {
            InputStream input = GetMimeTypeIconTag.class.getResourceAsStream("/mime-types.csv");
            CSVParser parser = CSVParser.parse(input, Charset.defaultCharset(), CSVFormat.EXCEL.withHeader());

            Map<String, MimeTypeValue> map = new HashMap<>();
            for (CSVRecord record : parser) {
                String mimeType = record.get("mime-type");
                String extension = record.get("extension");
                String description = record.get("description");

                MimeTypeValue value = new MimeTypeValue(mimeType, extension, description);
                map.put(mimeType, value);
            }

            mimeTypes = map;
        }
    }


    /**
     * Setter for mimeType.
     * 
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

}

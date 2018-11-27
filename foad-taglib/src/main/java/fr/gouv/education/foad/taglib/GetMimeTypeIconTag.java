package fr.gouv.education.foad.taglib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
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

        
        // Document type DOM element
        Element element = DOM4JUtils.generateElement("span", "document-type", StringUtils.EMPTY);
        
        // MIME type object
        MimeType mimeTypeObject;
        if (value == null) {
            try {
                mimeTypeObject = new MimeType(this.mimeType);
            } catch (MimeTypeParseException e) {
                mimeTypeObject = null;
            }
        } else {
            mimeTypeObject = value.getMimeType();
        }
        if (mimeTypeObject != null) {
            DOM4JUtils.addDataAttribute(element, "primary-type", mimeTypeObject.getPrimaryType());
            DOM4JUtils.addDataAttribute(element, "sub-type", mimeTypeObject.getSubType());
        }
        
        // MIME type extension
        String extension;
        if (value == null) {
            extension = null;
        } else {
            extension = value.getExtension();
        }
        if (StringUtils.isNotEmpty(extension)) {
            DOM4JUtils.addDataAttribute(element, "extension", extension);
        }
        
        // MIME type description
        String description;
        if (value == null) {
            description = null;
        } else {
            description = value.getDescription();
        }
        if (StringUtils.isNotEmpty(description)) {
            DOM4JUtils.addAttribute(element, "title", description);
        }
        
        // MIME type display
        String display;
        if (value == null) {
            display = null;
        } else {
            display = value.getDisplay();
        }
        if (StringUtils.isNotEmpty(display)) {
            DOM4JUtils.addDataAttribute(element, "display", display);
            DOM4JUtils.addDataAttribute(element, "length", String.valueOf(display.length()));
        }
        
        // MIME type icon
        String icon;
        if (StringUtils.isNotEmpty(display) || (value == null)) {
            icon = null;
        } else {
            icon = value.getIcon();
        }
        if (StringUtils.isNotEmpty(icon)) {
            Element iconElement = DOM4JUtils.generateElement("i", icon, StringUtils.EMPTY);
            element.add(iconElement);
            DOM4JUtils.addDataAttribute(element, "length", String.valueOf(1));
        }
        
        // Unknown MIME type indicator
        if (value == null) {
            DOM4JUtils.addDataAttribute(element, "unknown", StringUtils.EMPTY);
        }


        // HTML writer
        HTMLWriter htmlWriter = new HTMLWriter(this.getJspContext().getOut());
        htmlWriter.setEscapeText(false);
        htmlWriter.write(element);
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
                String display = record.get("display");
                String icon = record.get("icon");

                MimeTypeValue value = new MimeTypeValue(mimeType, extension, description, display, icon);
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

package fr.gouv.education.foad.taglib;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/**
 * MIME type value.
 * 
 * @author Cédric Krommenhoek
 */
public class MimeTypeValue {

    /** MIME type object. */
    private final MimeType mimeType;
    /** MIME type extension. */
    private final String extension;
    /** MIME type description. */
    private final String description;
    /** MIME type display. */
    private final String display;
    /** MIME type icon. */
    private final String icon;


    /**
     * Constructor.
     * 
     * @param mimeType MIME type
     * @param extension MIME type extension
     * @param description MIME type description
     * @param display MIME type display
     * @param icon MIME type icon
     */
    public MimeTypeValue(String mimeType, String extension, String description, String display, String icon) {
        super();
        this.extension = extension;
        this.description = description;
        this.display = display;
        this.icon = icon;

        // Mime type object
        MimeType mimeTypeObject;
        try {
            mimeTypeObject = new MimeType(mimeType);
        } catch (MimeTypeParseException e) {
            mimeTypeObject = null;
        }
        this.mimeType = mimeTypeObject;
    }


    /**
     * Getter for mimeType.
     * 
     * @return the mimeType
     */
    public MimeType getMimeType() {
        return mimeType;
    }

    /**
     * Getter for extension.
     * 
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for display.
     * 
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

}

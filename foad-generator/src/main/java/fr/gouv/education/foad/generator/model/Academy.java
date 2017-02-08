package fr.gouv.education.foad.generator.model;

import java.text.Normalizer;

import org.apache.commons.lang.StringUtils;

/**
 * Academy java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class Academy {

    /** Code. */
    private final String code;
    /** Normalized name. */
    private final String name;
    /** Label. */
    private final String label;


    /**
     * Constructor.
     *
     * @param code code
     * @param label label
     */
    public Academy(int code, String label) {
        super();
        this.code = StringUtils.leftPad(String.valueOf(code), 2, "0");
        this.name = Normalizer.normalize(label, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        this.label = label;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.code == null) ? 0 : this.code.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Academy other = (Academy) obj;
        if (this.code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!this.code.equals(other.code)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for code.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for label.
     *
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }


}

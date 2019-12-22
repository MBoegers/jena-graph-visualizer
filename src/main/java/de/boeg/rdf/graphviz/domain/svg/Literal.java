package de.boeg.rdf.graphviz.domain.svg;

import de.boeg.rdf.graphviz.domain.util.TrippleMapper;
import lombok.Getter;

/**
 * Represents a Literal property for the SVG representation, this equals to a line within a instance.
 * RDF speaking all Literals are parsed into this.
 */
public class Literal implements Property {

    /* Placeholder */
    private final static String PROPERTY_NAME = "<!--PROPERTY_NAME-->";
    private final static String LITERAL_VALUE = "<!--LITERAL_VALUE-->";
    private static final String LITERAL_Y_POSITION = "<!--LITERAL_Y-->"; /* Y-Position inside the instance sub SVG, y = 50 * i (i equals id) */
    private static final String LITERAL_ID = "<!--LITERAL_ID-->";
    private static final String PROP_NAME_Y = "<!--PROP_NAME_Y-->";
    private static final String PROP_VALUE_Y = "<!--PROP_VALUE_Y-->";

    /* Template */
    private static final String LITERAL_TEMPLATE = "\t\t<g id=\"" + LITERAL_ID + "\">\n" +
            "\t\t    <line class=\"splitter\" x1=\"0\" x2=\"320\" y1=\"" + LITERAL_Y_POSITION + "\" y2=\"" + LITERAL_Y_POSITION + "\"/>\n" +
            "\t\t    <text x=\"10\" y=\"" + PROP_NAME_Y + "\" class=\"property\">" + PROPERTY_NAME + "</text>\n" +
            "\t\t    <text x=\"15\" y=\"" + PROP_VALUE_Y + "\" class=\"literal\">" + LITERAL_VALUE + "</text>\n" +
            "\t\t</g>\n";


    /* Information */
    @Getter
    private Integer literalID = -1;
    @Getter
    private final String name;
    @Getter
    private final String value;
    @Getter
    private final String mrid;

    public Literal(TrippleMapper.StringTriple triple) {
        name = triple.getL();
        value = triple.getD();
        mrid = triple.getS();
    }

    void setLiteralID(int p) {
        literalID = p + 1; // from start at 0 to start at 1 system
    }

    @Override
    public String toSvgString() {
        int position = this.literalID * 50;
        String yPropPosition = Integer.toString(position + 10);
        String yValuePosition = Integer.toString(position + 30);
        return LITERAL_TEMPLATE
                .replace(PROPERTY_NAME, name)
                .replace(LITERAL_VALUE, value)
                .replace(LITERAL_ID, this.literalID.toString())
                .replace(LITERAL_Y_POSITION, Integer.toString(position))
                .replace(PROP_NAME_Y, yPropPosition)
                .replace(PROP_VALUE_Y, yValuePosition); /* calculate the actual position */
    }
}

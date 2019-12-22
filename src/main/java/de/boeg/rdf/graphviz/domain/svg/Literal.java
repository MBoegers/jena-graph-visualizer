package de.boeg.rdf.graphviz.domain.svg;

import de.boeg.rdf.graphviz.domain.util.NodeMapperUtil;

public class Property {

    private final static String PROPERTYNAME = "<!--PROPERTYNAME-->";
    private final static String LITERAL = "<!--LITERALVALUE-->";
    private static final String PROPERTY_Y = "<!--PROPERTY_Y-->"; /* Y-Position inside the instance sub SVG, y = 50 * i (i equals id) */
    private static final String POSITION = "<!--POSITION-->";

    private static final String PROPERTY = "<svg x=\"5\" y=\"" + PROPERTY_Y + "\" id=\"" + POSITION + "\">\n" +
            "    <line class=\"splitter\" x1=\"0\" x2=\"320\" y1=\"0\" y2=\"0\"/>\n" +
            "    <text x=\"5\" y=\"10\" class=\"property\">" + PROPERTYNAME + "</text>\n" +
            "    <text x=\"10\" y=\"30\" class=\"literal\">" + LITERAL + "</text>\n" +
            "</svg>\n";

    private final String name;
    private final String value;
    private Integer position = -1;

    Property(NodeMapperUtil.StringTriple triple) {
        name = triple.getL();
        value = triple.getD();
    }

    void setPosition(int p) {
        position = p + 1;
    }

    String toSVGString() {
        return PROPERTY
                .replace(PROPERTYNAME, name)
                .replace(LITERAL, value)
                .replace(POSITION, position.toString())
                .replace(PROPERTY_Y, Integer.toString(position * 50)); /* calculate the actual position */
    }
}

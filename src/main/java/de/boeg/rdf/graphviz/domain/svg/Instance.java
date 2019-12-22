package de.boeg.rdf.graphviz.domain.svg;

import de.boeg.graph.layout.domain.Node;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Instance extends Node<String> {
    private final List<Literal> literals;

    private final static String PROPERTY = "<!--PROPERTIES-HERE-->";
    private static final String CLASSNAME = "<!--CLASS-->";
    private static final String MRID = "<!--MRID-->";
    private static final String INSTANCE_HEIGHT = "<!--INSTANCE_HEIGHT-->"; // min hight: 50 + (#properties * 35)
    private static final String RECT_HEIGHT = "<!--RECT_HEIGHT-->"; // e.q. INSTANCE_HEIGHT minus padding
    private static final String INSTANCE_XPOS = "<!--INSTANCE_XPOS-->";
    private static final String INSTANCE_YPOS = "<!--INSTANCE_YPOS-->";


    private static final String INSTANCE = "\t<svg x=\"" + INSTANCE_XPOS + "\" y=\"" + INSTANCE_YPOS + "\" height=\"" + INSTANCE_HEIGHT + "\" width=\"330\"> \n" +
            "\t    <g>\n" +
            "\t        <rect x=\"5\" y =\"5\" width=\"320\" height=\"" + RECT_HEIGHT + "\" class=\"classbox\"/>\n" +
            "\t        <text x=\"150\" y=\"15\" class=\"classname\">" + CLASSNAME + "</text>\n" +
            "\t        <text x=\"15\" y=\"35\">" + MRID + "</text>\n" +
            "\t    <g/>\n" +
            PROPERTY +
            "\t</svg>\n";

    private final String mrid;
    private String className;
    private Double instanceX = 0d;
    private Double instanceY = 0d;

    @Getter
    private Integer height = 50;

    public Instance(String mrid, List<Literal> literals) {
        this.mrid = mrid;
        className = "UnknownClassName";
        this.literals = new ArrayList<>(literals.size());
        for (Literal l : literals) {
            this.add(l);
        }
    }

    private void add(Literal literal) {
        if ("rdf:type".equals(literal.getName())) {
            className = literal.getValue();
        } else {
            literal.setLiteralID(literals.size()); // set position
            literals.add(literal);
            height += 45;
        }
    }

    String toSVGString() {
        // parse to string
        String propertiesStr = literals.parallelStream()
                .map(Literal::toSvgString)
                .collect(Collectors.joining());
        String xPos = Long.toString(Math.round(instanceX));
        String yPos = Long.toString(Math.round(instanceY));
        //concat
        return INSTANCE
                .replace(INSTANCE_XPOS, xPos)
                .replace(INSTANCE_YPOS, yPos)
                .replace(CLASSNAME, className)
                .replace(MRID, mrid)
                .replace(INSTANCE_HEIGHT, Integer.toString(height))
                .replace(RECT_HEIGHT, Integer.toString(height - 10))
                .replace(PROPERTY, propertiesStr);
    }

    @Override
    public String getIdentifier() {
        return mrid;
    }

    @Override
    public double getX() {
        return instanceX;
    }

    @Override
    public double getY() {
        return instanceY;
    }

    @Override
    public void setX(double x) {
        instanceX = x;
    }

    @Override
    public void setY(double y) {
        instanceY = y;
    }
}

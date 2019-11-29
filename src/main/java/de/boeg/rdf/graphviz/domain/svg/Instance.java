package de.boeg.rdf.graphviz.domain.svg;

import de.boeg.rdf.graphviz.domain.util.NodeMapperUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Instance {
    private final List<Property> properties = new ArrayList<>();

    private final static String PROPERTY = "<!--PROPERTIES-HERE-->";
    private static final String CLASSNAME = "<!--CLASS-->";
    private static final String MRID = "<!--MRID-->";
    private static final String INSTANCE_HEIGHT = "<!--INSTANCE_HEIGHT-->"; // min hight: 50 + (#properties * 35)
    private static final String RECT_HEIGHT = "<!--RECT_HEIGHT-->"; // e.q. INSTANCE_HEIGHT minus padding
    private static final String INSTANCE_XPOS = "<!--INSTANCE_XPOS-->";
    private static final String INSTANCE_YPOS = "<!--INSTANCE_YPOS-->";


    private static final String INSTANCE = "<svg x=\"" + INSTANCE_XPOS + "\" y=\"" + INSTANCE_YPOS + "\" height=\"" + INSTANCE_HEIGHT + "\" width=\"330\"> \n" +
            "    <g>\n" +
            "        <rect x=\"5\" y =\"5\" width=\"320\" height=\"" + RECT_HEIGHT + "\" class=\"classbox\"/>\n" +
            "        <text x=\"150\" y=\"15\" class=\"classname\">" + CLASSNAME + "</text>\n" +
            "        <text x=\"15\" y=\"35\">" + MRID + "</text>\n" +
            "    <g/>\n" +
            PROPERTY +
            "</svg>\n";

    private final String mrid;

    private final String className;

    @Getter
    @Setter
    private Integer instanceX = 0;
    @Getter
    @Setter
    private Integer instanceY = 0;

    @Getter
    private Integer height = 50;

    public Instance(String mrid, List<NodeMapperUtil.StringTriple> triples) {
        this.mrid = mrid;
        className = "UnknownClassName";
        triples.parallelStream()
                .map(Property::new)
                .forEach(this::add);
    }

    private void add(Property property) {
        property.setPosition(properties.size()); // set position
        properties.add(property);
        height += 45;
    }

    String toSVGString() {
        // parse to string
        String propertiesStr = properties.parallelStream()
                .map(Property::toSVGString)
                .collect(Collectors.joining());
        //concat
        return INSTANCE
                .replace(INSTANCE_XPOS, instanceX.toString())
                .replace(INSTANCE_YPOS, instanceY.toString())
                .replace(CLASSNAME, className)
                .replace(MRID, mrid)
                .replace(INSTANCE_HEIGHT, Integer.toString(height))
                .replace(RECT_HEIGHT, Integer.toString(height - 10))
                .replace(PROPERTY, propertiesStr);
    }
}

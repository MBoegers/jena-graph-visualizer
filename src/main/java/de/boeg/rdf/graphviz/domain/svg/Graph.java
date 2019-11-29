package de.boeg.rdf.graphviz.domain.svg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {

    private final List<Instance> instances = new ArrayList<>();

    private Integer height = 100;
    private Integer width = 100;

    private final static String INSTANCE = "<!--INSTANCE-HERE-->";
    private final static String TOTAL_HEIGHT = "<!--HEIGHT-->";
    private final static String TOTAL_WIDTH = "<!--WIDTH-->";

    private static final String GRAPH = "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
            " height=\"500\" width=\"500\">\n" +
            "    <defs>\n" +
            "        <style type=\"text/css\">\n" +
            "        <![CDATA[\n" +
            "            .classbox {\n" +
            "            fill: yellow;\n" +
            "            stroke: black;\n" +
            "            stroke-width: 3;\n" +
            "            }\n" +
            "            .splitter {\n" +
            "                stroke: grey;\n" +
            "                stroke-width: 1;\n" +
            "            }\n" +
            "            .classname {\n" +
            "                font-size: x-small;\n" +
            "            }\n" +
            "            .mrid {\n" +
            "                font-size: x-large;\n" +
            "            }\n" +
            "            .property {\n" +
            "                font-size: x-small;\n" +
            "            }\n" +
            "            .literal {\n" +
            "                font-size: small;\n" +
            "            }\n" +
            "        ]]>\n" +
            "        </style>\n" +
            "    </defs>\n" +
            INSTANCE +
            "</svg>";


    public void add(Instance instance) {
        instances.add(instance);
        height += 20;
        width += 30;
    }

    public String toSVGString() {
        String instancesStr = instances.parallelStream()
                .map(Instance::toSVGString)
                .collect(Collectors.joining());
        return GRAPH.replace(INSTANCE, instancesStr)
                .replace(TOTAL_HEIGHT, height.toString())
                .replace(TOTAL_WIDTH, width.toString());
    }
}

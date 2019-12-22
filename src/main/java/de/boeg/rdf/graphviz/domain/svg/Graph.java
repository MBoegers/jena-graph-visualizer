package de.boeg.rdf.graphviz.domain.svg;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {

    @Getter
    private final List<Instance> instances = new ArrayList<>();
    @Getter
    private final List<Link> links = new ArrayList<>();

    @Getter
    private Integer height = 100;
    @Getter
    private Integer width = 100;

    private final static String INSTANCE = "<!--INSTANCE-HERE-->";
    private final static String LINKS = "<!--LINKS-HERE-->";
    private final static String TOTAL_HEIGHT = "<!--HEIGHT-->";
    private final static String TOTAL_WIDTH = "<!--WIDTH-->";

    private static final String GRAPH = "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
            " height=\"" + TOTAL_HEIGHT + "\" width=\"" + TOTAL_WIDTH + "\">\n" +
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
            LINKS +
            "\n</svg>";

    public synchronized void addInstance(Instance instance) {
        instances.add(instance);
        height += 20;
        width += 30;
    }

    public synchronized void addLink(Link link) {
        links.add(link);
    }

    public void scale(float factor) {
        height = Math.round(height * factor);
        width = Math.round(width * factor);
    }

    public String toSVGString() {
        String instancesStr = instances.parallelStream()
                .map(Instance::toSVGString)
                .collect(Collectors.joining());
        String linksStr = links.parallelStream()
                .map(Link::toSvgString)
                .collect(Collectors.joining());
        return GRAPH.replace(INSTANCE, instancesStr)
                .replace(TOTAL_HEIGHT, height.toString())
                .replace(TOTAL_WIDTH, width.toString())
                .replace(LINKS, linksStr);
    }
}

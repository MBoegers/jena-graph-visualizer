package de.boeg.rdf.graphviz.domain.svg;

public interface Property {
    /**
     * Generate the SVG representation of the property
     * @return SCG-String
     */
    String toSvgString();
}

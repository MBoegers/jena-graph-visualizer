package de.boeg.rdf.graphviz.usecase;

import org.apache.jena.graph.Triple;

import java.io.InputStream;
import java.util.List;

public interface ReadTriplesUseCase {
    List<Triple> fromStream(InputStream inputStream);
}

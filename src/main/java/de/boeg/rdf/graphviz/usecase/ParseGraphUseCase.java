package de.boeg.rdf.graphviz.usecase;

import org.apache.jena.graph.Triple;

import java.util.List;


public interface ParseGraphUseCase<T> {
    T fromTriples(List<Triple> tripleList);
}

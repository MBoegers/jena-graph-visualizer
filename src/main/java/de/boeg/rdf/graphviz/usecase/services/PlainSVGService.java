package de.boeg.rdf.graphviz.usecase.services;

import de.boeg.rdf.graphviz.domain.util.NodeMapperUtil;
import de.boeg.rdf.graphviz.domain.svg.Graph;
import de.boeg.rdf.graphviz.domain.svg.Instance;
import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("plain")
@Slf4j
public class PlainSVGService implements ParseGraphUseCase<String> {

    @Override
    public String fromTriples(List<Triple> tripleList) {
        Graph graph = new Graph();

        log.info("Received {} triples", tripleList.size());
        Map<String, List<NodeMapperUtil.StringTriple>> groupedTriples
                = tripleList.parallelStream()
                .map(NodeMapperUtil.jenaTriple2NodeTriple)
                .map(NodeMapperUtil.nodeTriple2StringTriple)
                .collect(Collectors.groupingByConcurrent(NodeMapperUtil.StringTriple::getS));

        log.info("Found {} instances", groupedTriples.size());
        groupedTriples.entrySet().parallelStream()
                .map(e -> new Instance(e.getKey(), e.getValue()))
                .forEach(graph::add);

        log.info("Graph to String parsing");
        return graph.toSVGString();
    }
}

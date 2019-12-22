package de.boeg.rdf.graphviz.usecase.services;

import de.boeg.graph.layout.GraphLayout;
import de.boeg.graph.layout.LayoutFactory;
import de.boeg.rdf.graphviz.domain.svg.Graph;
import de.boeg.rdf.graphviz.domain.svg.Instance;
import de.boeg.rdf.graphviz.domain.svg.Link;
import de.boeg.rdf.graphviz.domain.svg.Literal;
import de.boeg.rdf.graphviz.domain.util.TrippleMapper;
import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Qualifier("plain")
@Slf4j
public class PlainSVGService implements ParseGraphUseCase<String> {

    @Override
    public String fromTriples(List<Triple> tripleList) {

        log.info("Received {} triples", tripleList.size());

        // get triples as String triples and split them by isLiteral
        Map<Boolean, List<TrippleMapper.StringTriple>> stringTriples
                = tripleList.parallelStream()
                .map(TrippleMapper.jenaTriple2NodeTriple)
                .map(TrippleMapper.nodeTriple2StringTriple)
                .collect(Collectors.partitioningBy(TrippleMapper.StringTriple::isLiteral));

        // get the literals
        Map<String, List<Literal>> literals = stringTriples.get(true) // all literal triples
                .parallelStream()
                .map(Literal::new)
                .collect(Collectors.groupingBy(Literal::getMrid));

        Graph g = new Graph();

        // combine instances
        Map<String, Instance> instances = stringTriples.get(false).parallelStream()
                .map(TrippleMapper.StringTriple::getS)
                .distinct()
                .map(mrid -> new Instance(mrid, literals.getOrDefault(mrid, new ArrayList<>())))
                .collect(Collectors.toMap(Instance::getIdentifier, Function.identity()));

        instances.values().stream().forEach(g::addInstance);

        // generate links
        stringTriples.get(false).parallelStream()
                .filter(t -> instances.containsKey(t.getS()) && instances.containsKey(t.getD()))
                .map(t -> new Link(instances.get(t.getS()), instances.get(t.getD()), t.getL()))
                .forEach(g::addLink);

        log.info("Start graph layout");
        g.scale(0.75f);
        GraphLayout layout = LayoutFactory.newInstance()
                .forced()
                .height(g.getHeight())
                .width(g.getWidth())
                .nodes(g.getInstances())
                .edges(g.getLinks())
                .build();

        for (int i = 0; i < 100; i++) {
            layout.layout();
        }

        log.info("Graph to String parsing");
        return g.toSVGString();
    }
}

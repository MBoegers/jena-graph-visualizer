package de.boeg.rdf.graphviz.usecase.services.graphviz;

import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import guru.nidi.graphviz.model.Graph;
import lombok.var;
import org.apache.jena.graph.Triple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.graph;

@Service
@Qualifier("dot")
public class DOTStringService implements ParseGraphUseCase<String> {

    @Override
    public String fromTriples(List<Triple> tripleList) {
        var nodeList = tripleList.parallelStream()
                .map(DotGraphUtil.jenaTriple2NodeTriple)
                .map(DotGraphUtil.nodeTriple2StringTriple)
                .map(DotGraphUtil.stringTriple2GraphVizNode)
                .collect(Collectors.toList());

        Graph g = graph("example2").directed().with(nodeList);
        return g.toString();
    }
}

package de.boeg.rdf.graphviz.usecase.services.graphviz;

import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import de.boeg.rdf.graphviz.usecase.services.graphviz.DotGraphUtil;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.Graph;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.jena.graph.Triple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.graph;

@Service
@Slf4j
public class GraphVizService implements ParseGraphUseCase<Renderer> {

    @Value("${file.output}")
    private Path outputFile;

    @Override
    public Renderer fromTriples(List<Triple> tripleList) {
        var nodeList = tripleList.parallelStream()
                .limit(1000)
                .map(DotGraphUtil.jenaTriple2NodeTriple)
                .map(DotGraphUtil.nodeTriple2StringTriple)
                .map(DotGraphUtil.stringTriple2GraphVizNode)
                .collect(Collectors.toList());

        Graph g = graph("example2").directed().with(nodeList);

        Graphviz.useEngine(new GraphvizCmdLineEngine());

        return Graphviz.fromGraph(g)
                .width(3000)
                .height(3000)
                .render(Format.PNG);
    }
}

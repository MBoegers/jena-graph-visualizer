package de.boeg.rdf.graphviz.usecase.services.graphviz;

import guru.nidi.graphviz.attribute.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;

class DotGraphUtil {

    @AllArgsConstructor
    static class NodeTriple {
        private final Node s;
        private final Node p;
        private final Node o;
    }

    @Getter
    @AllArgsConstructor
    static class StringTriple {
        private final String s;
        private final String l;
        private final String d;
    }

    static Function<Triple, NodeTriple> jenaTriple2NodeTriple = t ->
            new NodeTriple(t.getMatchSubject(), t.getMatchPredicate(), t.getMatchObject());

    static Function<NodeTriple, StringTriple> nodeTriple2StringTriple = t ->
            new StringTriple(nodeAsString(t.s), nodeAsString(t.p), nodeAsString(t.o));

    static Function<StringTriple, guru.nidi.graphviz.model.Node> stringTriple2GraphVizNode = t ->
            node(t.s).link(to(node(t.d)).with(Label.of(t.l)));

    static Function<guru.nidi.graphviz.model.Node, String> graphVizNode2DotString = guru.nidi.graphviz.model.Node::toString;


    private static String nodeAsString(Node n) {
        String nodeStr;
        if (n.isURI()) {
            nodeStr = trimNamespace(n.getURI());
        } else if (n.isLiteral()) {
            nodeStr = n.getLiteralLexicalForm();
        } else if (n.isBlank()) {
            nodeStr = "";
        } else {
            nodeStr = n.toString();
        }
        return nodeStr.replaceAll("-", "");
    }

    private static String trimNamespace(String url) {
//        if (url.startsWith("file:///media/merlin/Austausch/Projekte/Luzzu/graph-viz/#_")) {
//            url = url.replace("file:///media/merlin/Austausch/Projekte/Luzzu/graph-viz/#_", "");
//        }
        if (url.startsWith("http://iec.ch/TC57/2013/CIM-schema-cim16#")) {
            url = url.replace("http://iec.ch/TC57/2013/CIM-schema-cim16#", "cim:");
        }
        return url;
    }

}

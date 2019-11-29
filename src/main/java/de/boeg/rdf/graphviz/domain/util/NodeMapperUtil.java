package de.boeg.rdf.graphviz.domain.util;

import guru.nidi.graphviz.attribute.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;

@UtilityClass
public class NodeMapperUtil {

    @AllArgsConstructor
    public static class NodeTriple {
        private final Node s;
        private final Node p;
        private final Node o;
    }

    @Getter
    @AllArgsConstructor
    public static class StringTriple {
        private final String s;
        private final String l;
        private final String d;
    }

    public static final Function<Triple, NodeTriple> jenaTriple2NodeTriple = t ->
            new NodeTriple(t.getMatchSubject(), t.getMatchPredicate(), t.getMatchObject());

    public static final Function<NodeTriple, StringTriple> nodeTriple2StringTriple = t ->
            new StringTriple(nodeAsString(t.s), nodeAsString(t.p), nodeAsString(t.o));

    public static final Function<StringTriple, guru.nidi.graphviz.model.Node> stringTriple2GraphVizNode = t ->
            node(t.s).link(to(node(t.d)).with(Label.of(t.l)));

    public static final Function<guru.nidi.graphviz.model.Node, String> graphVizNode2DotString = guru.nidi.graphviz.model.Node::toString;


    private String nodeAsString(Node n) {
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

    private String trimNamespace(String url) {
        if (url.startsWith("file://")) { // remove local namespace
            return url.substring(url.indexOf("/#_") + 1);
        }
        // remove real namespaces
        return url.replace("http://iec.ch/TC57/2013/CIM-schema-cim16#", "cim:")
                .replace("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:")
                .replace("http://iec.ch/TC57/61970-552/ModelDescription/1#", "md:");
    }

}

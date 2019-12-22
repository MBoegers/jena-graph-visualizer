package de.boeg.rdf.graphviz.domain.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;

import java.util.function.Function;

@UtilityClass
public class TrippleMapper {

    @AllArgsConstructor
    /*
      Represents a triple with {@linkplain Node}
     */
    public static class NodeTriple {
        private final Node s;
        private final Node p;
        private final Node o;
    }

    @Getter
    @AllArgsConstructor
    /*
     * Represents a triple with only Strings
     */
    public static class StringTriple {
        private final String s;
        private final String l;
        private final String d;
        private final boolean isLiteral;
    }

    /**
     * Map a {@link Triple} to {@link NodeTriple} by getting the Subject, Predicate and Object as {@link Node}
     */
    public static final Function<Triple, NodeTriple> jenaTriple2NodeTriple = t ->
            new NodeTriple(t.getMatchSubject(), t.getMatchPredicate(), t.getMatchObject());

    /**
     * Map a {@link NodeTriple} to  {@link StringTriple} by parsing the {@link Node} to the representing String
     */
    public static final Function<NodeTriple, StringTriple> nodeTriple2StringTriple = t ->
            new StringTriple(nodeAsString(t.s), nodeAsString(t.p), nodeAsString(t.o), t.o.isLiteral() || RDF.type.asNode().equals(t.p));

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

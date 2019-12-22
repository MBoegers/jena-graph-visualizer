package de.boeg.rdf.graphviz.domain.svg;

import de.boeg.graph.layout.domain.Edge;
import de.boeg.graph.layout.domain.Node;
import lombok.Getter;

/**
 * Represents a not Literal Property for the instance
 */
public class Link extends Edge<String> implements Property {

    private final String X1 = "<!--X1-->";
    private final String X2 = "<!--X2-->";
    private final String Y1 = "<!--Y1-->";
    private final String Y2 = "<!--Y2-->";
    private final String LABEL = "<!--LABEL-->";
    private final String LBL_X = "<!--LABEL_X-->";
    private final String LBL_Y = "<!--LABEL_Y-->";

    private final String TEMPLATE = "\n\t<g>" +
            "\n\t    <text x=\"" + LBL_X + "\" y=\"" + LBL_Y + "\" fill=\"red\">" + LABEL + "</text>" +
            "\n\t    <line x1=\"" + X1 + "\"" +
            " x2=\"" + X2 + "\"" +
            " y1=\"" + Y1 + "\"" +
            " y2=\"" + Y2 + "\" style=\"stroke:rgb(255,0,0);stroke-width:2\" />" +
            "\n\t<g/>";

    @Getter
    private final String label;

    public Link(Node<String> first, Node<String> second, String label) {
        super(first, second);
        this.label = label;
    }

    @Override
    public String toSvgString() {
        String x = Double.toString((getX1() +  getX2()) / 2);
        String y = Double.toString((getY1() +  getY2()) / 2);
        return TEMPLATE
                .replace(X1, Double.toString(getX1() + 100))
                .replace(Y1, Double.toString(getY1()+ 50))
                .replace(X2, Double.toString(getX2() + 100))
                .replace(Y2, Double.toString(getY2() + 50))
                .replace(LABEL, label)
                .replace(LBL_X, x)
                .replace(LBL_Y, y);
    }
}

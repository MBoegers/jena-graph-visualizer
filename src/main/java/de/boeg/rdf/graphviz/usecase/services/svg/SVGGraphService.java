package de.boeg.rdf.graphviz.usecase.services.svg;

import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.jena.graph.Triple;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;


@Service
public class SVGGraphService implements ParseGraphUseCase<String> {

    @Override
    public String fromTriples(List<Triple> tripleList) {
        Document doc = createDocument();
        return this.parseToString(doc);
    }

    private Document createDocument() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument("http://www.w3.org/2000/svg", "svg", null);


        // Create a 'g' element and append it to the root 'svg' element
        Element g = doc.createElementNS("http://www.w3.org/2000/svg", "g");

        Element rect = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("x", "20");
        rect.setAttribute("y", "5");
        rect.setAttribute("width", "180");
        rect.setAttribute("height", "80");
        rect.setAttribute("rx", "10");
        rect.setAttribute("ry", "10");

        Element text = doc.createElementNS("http://www.w3.org/2000/svg", "text");
        text.setAttribute("x", "105");
        text.setAttribute("y", "65");


        g.appendChild(rect);
        g.appendChild(text);
        doc.getDocumentElement().appendChild(g);
        doc.normalizeDocument();
        return doc;
    }

    //method to convert Document to String
    private String parseToString(Document doc)
    {
        try
        {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch(TransformerException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}

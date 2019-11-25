package de.boeg.rdf.graphviz.usecase.services;

import de.boeg.rdf.graphviz.usecase.ReadTriplesUseCase;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class ReadRDFService implements ReadTriplesUseCase {

    @Override
    public List<Triple> fromStream(InputStream inputStream) {
        Model model = readModel(inputStream);
        List<Triple> triples = modelToList(model);
        return Collections.unmodifiableList(triples);
    }

    private Model readModel(InputStream stream) {
        Model model = ModelFactory.createDefaultModel();
        model.read(stream, "");
        return model;
    }

    private List<Triple> modelToList(Model model) {
        return model.listStatements()
                .mapWith(statement -> statement.asTriple())
                .toList();
    }
}

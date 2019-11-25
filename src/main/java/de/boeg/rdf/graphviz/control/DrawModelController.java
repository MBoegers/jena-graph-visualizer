package de.boeg.rdf.graphviz.control;

import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import de.boeg.rdf.graphviz.usecase.services.ReadRDFService;
import guru.nidi.graphviz.engine.Renderer;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.jena.graph.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@Slf4j
public class DrawModelController {


    @Value("${file.input}")
    private Path inputFile;
    @Value("${file.output}")
    private Path outputFile;
    @Autowired
    private ReadRDFService readRDFService;
    @Autowired
    private ParseGraphUseCase<String> drawForcedSVGGraphUC;
    @Autowired
    private ParseGraphUseCase<Renderer> darwWithGraphViz;
    @Autowired
    private ParseGraphUseCase<Void> internalGraphViz;

    @RequestMapping("/draw/model")
    public void landingPage() throws IOException {
        List<Triple> triples = null;
        try (InputStream inputStream = Files.newInputStream(inputFile)) {
            triples = readRDFService.fromStream(inputStream);

        } catch (IOException e) {
            log.error("Failed to read resource", e);
        }

//        this.internalGraphViz.fromTriples(triples);

//        String dotGraph = drawForcedSVGGraphUC.fromTriples(triples);
//
//        if(Files.exists(outputFile)) {
//            Files.delete(outputFile);
//        }
//        Files.createFile(outputFile);
//        try(BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardOpenOption.WRITE)) {
//            writer.write(dotGraph, 0, dotGraph.length());
//            writer.flush();
//        }

        var svgImg = darwWithGraphViz.fromTriples(triples);
        try {
            svgImg.toFile(outputFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Done Parsing to: " + outputFile.toString());
    }
}

package de.boeg.rdf.graphviz.control;

import de.boeg.rdf.graphviz.usecase.ParseGraphUseCase;
import de.boeg.rdf.graphviz.usecase.services.ReadRDFService;
import guru.nidi.graphviz.engine.Renderer;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.jena.graph.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RestController
@Slf4j
public class DrawModelController {

    private final Path inputFile;
    private final Path outputFile;
    private final ReadRDFService readRDFService;
    private final ParseGraphUseCase<String> drawForcedSVGGraphUC;

    @Autowired
    public DrawModelController(ReadRDFService readRDFService,
                               ParseGraphUseCase<String> drawForcedSVGGraphUC,
                               @Value("${file.input}") Path inputFile,
                               @Value("${file.output}") Path outputFile) {
        this.readRDFService = readRDFService;
        this.drawForcedSVGGraphUC = drawForcedSVGGraphUC;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @RequestMapping("/draw/model")
    public void landingPage() throws IOException {
        List<Triple> triples = null;
        log.info("Start reading model from {}", inputFile.toString());
        try (InputStream inputStream = Files.newInputStream(inputFile)) {
            triples = readRDFService.fromStream(inputStream);

        } catch (IOException e) {
            log.error("Failed to read resource", e);
        }

        log.info("Start parsing SVG");
        String svg = drawForcedSVGGraphUC.fromTriples(triples);

        log.info("Start writing SVG");
        if(Files.exists(outputFile)) {
            Files.delete(outputFile);
        }
        Files.createFile(outputFile);
        try(BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardOpenOption.WRITE)) {
            writer.write(svg, 0, svg.length());
            writer.flush();
        }
        log.info("Done writing to: {}", outputFile.toString());
    }
}

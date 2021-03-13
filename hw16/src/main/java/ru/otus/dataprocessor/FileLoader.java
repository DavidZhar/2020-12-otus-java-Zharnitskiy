package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLoader implements Loader {
    private String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("MeasurementDeserializer");
            module.addDeserializer(Measurement.class, new JsonDeserializer<>() {
                @Override
                public Measurement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                    JsonNode node = p.readValueAsTree();
                    String name = node.has("name") ? node.get("name").asText() : "";
                    double value = node.has("value") ? node.get("value").doubleValue() : 0.0;
                    return new Measurement(name, value);
                }
            });
            mapper.registerModule(module);

            Path filePath = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
            Stream<String> lines = Files.lines(filePath);
            String content = lines.collect(Collectors.joining("\n"));
            lines.close();

            return Arrays.asList(mapper.readValue(content, Measurement[].class));
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}

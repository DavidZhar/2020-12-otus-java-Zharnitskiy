package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLoader implements Loader {
    private final String fileName;
    private final ObjectMapper mapper;

    public FileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))) {
            String content = lines.collect(Collectors.joining("\n"));

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
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Measurement.class);

            return mapper.readValue(content, type);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}

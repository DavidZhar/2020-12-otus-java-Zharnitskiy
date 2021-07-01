package ru.otus.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.web.template.TemplateProcessor;
import ru.otus.web.template.TemplateProcessorImpl;

public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        ClientServer server = new ClientServer(WEB_SERVER_PORT,
                gson, templateProcessor);

        server.start();
        server.join();
    }
}

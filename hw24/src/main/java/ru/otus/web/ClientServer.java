package ru.otus.web;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.web.servlet.ClientServlet;
import ru.otus.web.servlet.LoginServlet;
import ru.otus.web.template.TemplateProcessor;

public class ClientServer {
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public ClientServer(int port, Gson gson, TemplateProcessor templateProcessor) {
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    public void join() throws Exception {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {
        ServletContextHandler servletContextHandler = createServletContextHandler();
        HandlerList handlers = new HandlerList();
        handlers.addHandler(servletContextHandler);

        server.setHandler(handlers);
        return server;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientServlet(templateProcessor)), "/client");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor)), "/");
        return servletContextHandler;
    }
}

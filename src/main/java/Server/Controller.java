package Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;

public class Controller
{
    Server server = new Server(7070);
    ServletContextHandler handler = new ServletContextHandler(server, "/");

    public void start() throws Exception
    {
        handler.addServlet(ResponseServer.class, "/list");
        handler.addServlet(ResponseServer.class, "/get/*");
        handler.addServlet(ResponseServer.class, "/put/*");

        server.start();
    }
}

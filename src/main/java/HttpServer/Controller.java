package HttpServer;

import Storage.DataBase;
import Storage.ExampleProvider;
import Storage.FileInfo;
import Storage.Provider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Controller extends HttpServlet
{

    private static Logger logger = Logger.getAnonymousLogger();

    public static void setLogger(Logger logger)
    {
        Controller.logger = logger;
    }

    static Provider provider;

    static
    {
        try
        {
            provider = new DataBase();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static GregorianCalendar getDate(String stringDate)
    {
        GregorianCalendar date = new GregorianCalendar();
        String parts[] = stringDate.split(":");
        Integer values[] = Arrays.stream(parts).map(Integer::valueOf).toArray(Integer[]::new);
        date.set(values[0], values[1], values[2], values[3], values[4]);
        date.set(Calendar.SECOND, Integer.valueOf(parts[5]));
        return date;
    }

    private byte[] extract(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while((read = inputStream.read(buffer, 0, buffer.length)) != -1)
            byteStream.write(buffer, 0, read);
        byteStream.flush();
        return byteStream.toByteArray();
    }

    private static FileInfo getFileInfo(HttpServletRequest req)
    {
        String parts[] = req.getRequestURI().substring(10).split(";");
        GregorianCalendar date = getDate(parts[1]);
        return new FileInfo(parts[0], date);
    }

    private static String convertToString(GregorianCalendar date)
    {
        return String.format("%d:%d:%d:%d:%d:%d", date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                             date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY),
                             date.get(Calendar.MINUTE), date.get(Calendar.SECOND));
    }

    private static void sendErrorMessage(Exception e, HttpServletResponse resp)
    {
        try
        {
            resp.setStatus(51);
            logger.warning(String.format("Sending exception of type %s and msg: %s",
                                         e.getClass().toString(), e.getMessage()));
            ObjectOutputStream stream = new ObjectOutputStream(resp.getOutputStream());
            stream.writeObject(e);
            stream.flush();
        }
        catch(IOException nested)
        {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.warning("The error message hasn't been sent. Msg: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException
    {
        if(req.getRequestURI().startsWith("/list"))
            listFiles(req, resp);
        else if(req.getRequestURI().startsWith("/download"))
            getFile(req, resp);
        else
        {
            resp.setStatus(400);
            logger.warning(String.format("Incorrect request URI: %s", req.getRequestURI()));
        }
    }

    private void getFile(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
            FileInfo info = getFileInfo(req);
            resp.getOutputStream().write(provider.downloadFile(info));
            resp.getOutputStream().flush();
            resp.setStatus(200);
        }
        catch(Exception e)
        {
            sendErrorMessage(e, resp);
        }
    }

    private void listFiles(HttpServletRequest req, HttpServletResponse resp)
    {
        ObjectOutputStream stream = null;
        try
        {
            stream = new ObjectOutputStream(resp.getOutputStream());
            stream.writeObject(new ArrayList<>(provider.getFileNames()));
            stream.flush();
            resp.setStatus(200);
        }
        catch(Exception e)
        {
            sendErrorMessage(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    {
        resp.setStatus(200);
        try
        {
            String filename = req.getRequestURI().substring(8);
            byte[] file = extract(req.getInputStream());
            provider.uploadFile(filename, file);
        }
        catch(Exception e)
        {
            sendErrorMessage(e, resp);
        }
    }

    public static void start() throws Exception
    {
        Server server = new Server(7070);
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addServlet(Controller.class, "/list");
        handler.addServlet(Controller.class, "/upload/*");
        handler.addServlet(Controller.class, "/download/*");
        server.start();
    }
}

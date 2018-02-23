package Server;

import Storage.ExampleProvider;
import Storage.FileInfo;
import Storage.Provider;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.eclipse.jetty.http.HttpStatus;
import sun.misc.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.text.ParseException;
import java.util.*;

public class ResponseServer extends HttpServlet
{

    //Server is based on HTTD protocol
    Provider provider = new ExampleProvider();

    {
        try
        {
            for(FileInfo f : provider.getFileNames())
                System.out.println(String.format("%s\n%s", f.getFilename(), convertToString(f.getDate())));
        }
        catch(IOException e)
        { }
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
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1)
            byteStream.write(buffer, 0, read);
        byteStream.flush();
        return byteStream.toByteArray();
    }

    private static FileInfo getFileInfo(HttpServletRequest req)
    {
        String parts[] = req.getRequestURI().substring(5).split(";");
        GregorianCalendar date = getDate(parts[1]);
        return new FileInfo(parts[0], date);
    }

    private static String convertToString(GregorianCalendar date)
    {
        return String.format("%d:%d:%d:%d:%d:%d", date.get(Calendar.YEAR),
                             date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY),
                             date.get(Calendar.MINUTE), date.get(Calendar.SECOND));
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if(req.getRequestURI() == null)
            resp.setStatus(503);
        if(req.getRequestURI().equals("/list"))
            listFiles(req, resp);
        else if(req.getRequestURI().startsWith("/get"))
            getFile(req, resp);
    }

    private void getFile(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
            FileInfo info = getFileInfo(req);
            resp.getOutputStream().write(provider.downloadFile(info));
            resp.setStatus(200);
        }
        catch(Exception e)
        {
            System.out.println(String.format("Error message of type %s. Msg: %s", e.getClass().toString(), e.getMessage()));
            resp.setStatus(400);
        }
    }

    private void listFiles(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
            Object list = new ArrayList<>(provider.getFileNames());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutput out;
            out = new ObjectOutputStream(stream);
            out.writeObject(list);
            out.flush();
            resp.getOutputStream().write(stream.toByteArray());
            resp.setStatus(200);
        }
        catch(Exception e)
        {
            System.out.println(String.format("Error message of type %s. Msg: %s", e.getClass().toString(), e.getMessage()));
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            String filename = req.getRequestURI().substring(5);
            byte[] file = extract(req.getInputStream());
            provider.uploadFile(filename, file);
            resp.setStatus(200);
        }
        catch(Exception e)
        {
            System.out.println(String.format("Error message of type %s. Msg: %s", e.getClass().toString(), e.getMessage()));
            resp.setStatus(400);
        }
    }

    public static void main(String[] args)
    {
//        GregorianCalendar date = getDate("2014:1:2:8:9:10");
//        System.out.println(String.format("%d/%d/%d %d/%d/%d", date.get(Calendar.DATE),
//                                         date.get(Calendar.MONTH), date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY),
//                                         date.get(Calendar.MINUTE), date.get(Calendar.SECOND)));
    }
}

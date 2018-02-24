package Storage;

import FileValidator.XML.XMLValidator;
import HttpServer.Controller;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class DataBase implements Provider
{
    private static Logger logger = Logger.getAnonymousLogger();

    public static void setLogger(Logger logger)
    {
        DataBase.logger = logger;
    }

    final private String databaseURL = "jdbc:sqlite:xmlFiles.db";

    HashFunction sha256 = Hashing.sha256();

    private Connection connection;
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseURL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    XMLValidator validator = new XMLValidator();

    public DataBase() throws SQLException
    {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS db (filename text, _date INTEGER , " +
                "file blob, SHA text, PRIMARY KEY(filename, _date))";
        statement.execute(query);
    }

    @Override
    public Collection<FileInfo> getFileNames() throws Exception
    {
        String query = "SELECT filename, _date, sha FROM db";
        Set<FileInfo> set = new TreeSet<>();

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                String filename = rs.getString("filename");

                GregorianCalendar _date = new GregorianCalendar();
                long epoch = rs.getLong("_date");
                _date.setTimeInMillis(epoch);
                set.add(new FileInfo(filename, _date));
            }
        }
        catch(SQLException e)
        {
            logger.warning(String.format("getFileNames() error: %s", e.getMessage()));
        }

        return set;
    }

    @Override
    public void uploadFile(String filename, byte[] file) throws Exception
    {
        long _date = (new GregorianCalendar()).getTimeInMillis();
        validator.validate(file);
        String query = "INSERT INTO db VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, filename);
            stmt.setLong(2, _date);
            stmt.setBytes(3, file);
            stmt.setString(4,sha256.hashBytes(file).toString());
            System.out.println(sha256.hashBytes(file).toString());
            stmt.executeUpdate();
        } catch(SQLException e)
        {
            logger.warning(String.format("uploadFile Error: %s", e.getMessage()));
        }
    }

    @Override
    public byte[] downloadFile(FileInfo info) throws IOException {

        String filename = info.getFilename();
        String query = "SELECT file FROM db ORDER BY _date ASC limit 1";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            rs.next();
            byte[] bytes = rs.getBytes("file");
            return bytes;
        } catch(SQLException e) {
            logger.warning(String.format("downloadFile error %s", e.getMessage()));
        }
        return null;
    }
}

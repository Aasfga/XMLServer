package Storage;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

public class DataBase implements Provider{
    final private String databaseURL = "jdbc:sqlite:history.db";

    private Connection connection = DriverManager.getConnection(databaseURL);


    public DataBase() throws SQLException{
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS db (filename text, _date date, " +
                "file blob, SHA text, PRIMARY KEY(filename, _date))";
        statement.execute(query);
    }

    @Override
    public Collection<FileInfo> getFileNames() throws IOException {
        String query = "SELECT filename, _date, sha FROM db";
        Set<FileInfo> set = new TreeSet<>();

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                String filename = rs.getString("filename");
                GregorianCalendar _date = new GregorianCalendar();
                _date.setTime(rs.getDate("_date"));
                set.add(new FileInfo(filename, _date));
            }
        }catch(SQLException e) {
            // zrob tutaj cos fajnego
        }

        return set;
    }

    @Override
    public void uploadFile(String filename, byte[] file) throws IOException, JAXBException {
        
    }

    @Override
    public byte[] downloadFile(FileInfo info) throws IOException {
        return new byte[0];
    }
}

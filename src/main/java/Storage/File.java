package Storage;


import java.util.Date;

public interface File
{
    String getSHA();

    String getName();

    byte[] getData();

    Date getModificationDate();
}

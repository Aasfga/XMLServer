package Storage;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;

public interface Provider
{
    Collection<FileInfo> getFileNames() throws Exception;

    void uploadFile(String filename, byte[] file) throws Exception;

    byte[] downloadFile(FileInfo info) throws Exception;
}

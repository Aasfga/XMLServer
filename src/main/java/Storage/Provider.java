package Storage;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;

public interface Provider
{
    Collection<FileInfo> getFileNames() throws IOException;

    void uploadFile(String filename, byte[] file) throws IOException, JAXBException;

    byte[] downloadFile(FileInfo info) throws IOException;
}

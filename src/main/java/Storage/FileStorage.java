package Storage;

import java.util.List;

public interface FileStorage
{
    void insertFile(File file);

    List<File> getFiles();
}

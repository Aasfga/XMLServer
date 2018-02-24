package Storage;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class ExampleProvider implements Provider
{
    private static final int MIN_DAY    = 10;
    private static final int MAX_DAY    = 100;
    private static final int MIN_HOUR   = 0;
    private static final int MAX_HOUR   = 24;
    private static final int MIN_SIZE   = 10;
    private static final int MAX_SIZE   = 15;
    private static final int MIN_DUP    = 1;
    private static final int MAX_DUP    = 1;
    private static final int MIN_LEN    = 5;
    private static final int MAX_LEN    = 10;
    private static final int MIN_FSIZE  = 100;
    private static final int MAX_FSIZE  = 300;
    private static final int P_BASE     = 100;
    private static final int P_VAL      = 20;

    private Map<FileInfo, byte[]> files = new TreeMap<>();
    static private Random random = new Random();

    private static String generateWord(int length)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < length; i++)
        {
            builder.append((char) ('a' + random.nextInt(26)));
        }
        return builder.toString();
    }

    private static int nextInt(int a, int b)
    {
        if(a == b)
            return a;
        return a + random.nextInt(b - a);
    }

    private static GregorianCalendar generateDate()
    {
        GregorianCalendar date = new GregorianCalendar();
        long hour = 1000 * 60 * 60 * nextInt(MIN_HOUR, MAX_HOUR);
        long day = 1000 * 60 * 60L * 24L * nextInt(MIN_DAY, MAX_DAY);
        date.setTimeInMillis(new Date().getTime() - day - hour);
        return date;
    }

    public ExampleProvider()
    {
        for(int i = 0, maxI = nextInt(MIN_SIZE, MAX_SIZE); i < maxI; i++)
        {
            String filename = generateWord(nextInt(MIN_LEN, MAX_LEN)) + ".xml";
            for(int d = 0, maxD = nextInt(MIN_DUP, MAX_DUP); d < maxD; d++)
            {
                byte file[] = generateWord(nextInt(MIN_FSIZE, MAX_FSIZE)).getBytes();
                GregorianCalendar date = generateDate();
                files.put(new FileInfo(filename, date), file);
            }
        }
    }

    @Override
    public Collection<FileInfo> getFileNames()
    {
        return files.keySet();
    }

    @Override
    public void uploadFile(String filename, byte[] file) throws IOException
    {
        if(random.nextInt(P_BASE) < P_VAL)
            throw new IOException("Random connection error. Try again");
        if(filename == null)
            throw new NullPointerException();
        Collection<FileInfo> keys = files.keySet();


        if(keys.stream().anyMatch(info -> info.getFilename().equals(filename)))
        {
            FileInfo min = keys.stream().
                    filter(info -> info.getFilename().equals(filename)).
                    min(FileInfo::compareTo).get();
            if(Arrays.equals(files.get(min), file))
                throw new FileAlreadyExistsException("The same file exists");
        }
        files.put(new FileInfo(filename, new GregorianCalendar()), file);


    }

    @Override
    public byte[] downloadFile(FileInfo info) throws IOException
    {
        if(random.nextInt(P_BASE) < P_VAL)
            throw new IOException("Random Connection error. Try again");
        if(info == null)
            throw new NullPointerException();
        return files.getOrDefault(info, null);
    }

    public static void main(String[] args)
    {
        for(int i = 0; i < 10; i++)
        {
            GregorianCalendar date = generateDate();
            System.out.println(String.format("%d/%d/%d %d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR)));
        }
    }
}

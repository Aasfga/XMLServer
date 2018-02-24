package Storage;

import java.sql.SQLException;
import java.util.GregorianCalendar;

import static java.lang.Thread.sleep;

public class TesterDataBase {
    public static void main(String args[]) throws Exception{
        DataBase myDataBase = new DataBase();
        byte[] b_array = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        myDataBase.uploadFile("andrzej", b_array);
        myDataBase.uploadFile("andrzej2", b_array);
        myDataBase.uploadFile("andrzej3", b_array);
        //sleep(3000);
        myDataBase.uploadFile("andrzej4", b_array);
        System.out.println(myDataBase.getFileNames());
        byte[] bytes = myDataBase.downloadFile(new FileInfo("andrzej", new GregorianCalendar()));
        System.out.println(bytes);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}

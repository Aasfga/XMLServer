import HttpServer.Controller;
import Storage.DataBase;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    private static Logger appLogger = Logger.getLogger("AppLogger");

    static
    {
        Level level = Level.ALL;
        appLogger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        appLogger.setLevel(level);
        appLogger.addHandler(handler);
    }

    public static void main(String[] args) throws Exception
    {
        Controller.setLogger(appLogger);
        DataBase.setLogger(appLogger);
        Controller.start();
    }
}

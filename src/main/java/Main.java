
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ForkJoinPool;


public class Main {

    public static void main(String[] args) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/config.properties"));
            String host = prop.getProperty("db.host");
            String login = prop.getProperty("db.login");
            String password = prop.getProperty("db.password");

            String filePath = prop.getProperty("file.path");
            String URL = prop.getProperty("URL");
            int parallelism = Integer.parseInt(prop.getProperty("parallelism"));

            SiteTree linkChecker = new SiteTree(URL);
            String links = new ForkJoinPool(parallelism).invoke(linkChecker);
            Writer.writeToFile(links, filePath);
            Writer.writeToConsole(links);
            Writer.writeToDB(links, host, login,password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Writer {

    public static void writeToFile (String links, String filePath){
        List<String> linkList = Arrays.asList(links.split("\n"));
        List<String> siteMap = linkList.stream()
                .sorted(Comparator.comparing(l -> l))
                .map(l -> StringUtils.repeat('\t', StringUtils.countMatches(l, "/") - 2) + l)
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(filePath), siteMap);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Карта создана!");
    }

    public static void writeToConsole(String links){
        List<String> linkList = Arrays.asList(links.split("\n"));
        linkList.stream()
                .sorted(Comparator.comparing(l -> l))
                .forEach(System.out::println);
    }
    public static void writeToDB(String links, String DBName, String DBLogin, String DBPass){
        List<String> linkList = Arrays.asList(links.split("\n"));
        StringBuilder SQLLink = new StringBuilder();
        for(int i = 0; i<linkList.size(); i++){
            SQLLink.append("(").append(i + 1).append(", '").append(linkList.get(i)).append("'), ");
        }
        SQLLink.setLength(SQLLink.length()-2);

        try {
            Connection connection = DriverManager.getConnection(DBName, DBLogin, DBPass);

            connection.createStatement().execute("DROP TABLE IF EXISTS link");
            connection.createStatement().execute("CREATE TABLE link(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name TINYTEXT NOT NULL, " +
                    "PRIMARY KEY(id))");
            connection.createStatement().execute("INSERT INTO link VALUES " + SQLLink + ";");

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}

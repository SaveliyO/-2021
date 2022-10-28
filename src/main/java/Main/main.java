package Main;
import java.sql.*;

public class main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String userName = "root";
        String password = "pass";
        String connectionURl = "jdbc:mysql://localhost:3306/clientServer";
        try(Connection conn = DriverManager.getConnection(connectionURl, userName, password);
            Statement statement = conn.createStatement();){
            System.out.println("Подключение успешно");
            statement.executeUpdate("drop table issues");
            statement.executeUpdate("create table issues (id int not null auto_increment, description char(30), priority char(20), primary key (id));");
            statement.executeUpdate("insert into issues set description = 'first issue1', priority = 'ads1'");
            statement.executeUpdate("insert into issues set description = 'first issue2', priority = 'ads2'");
            statement.executeUpdate("insert into issues set description = 'first issue3', priority = 'ads3'");
            ResultSet resultSet = statement.executeQuery("select * from issues");

            while (resultSet.next()){
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
                System.out.println(resultSet.getString(3));
            }

        }


    }
}

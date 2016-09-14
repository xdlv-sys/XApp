package xd.fw.test;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SyncUsers {
    @Test
    public void syncUsers() throws Exception{

        Class.forName("com.mysql.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://114.55.129.254:3306/igecono_js?useUnicode=true&characterEncoding=utf-8",
                "igecono_js","igecono_2016!")){
            ResultSet resultSet = connection.createStatement().executeQuery("" +
                    "select user_id from t_jkn_user where user_id=3");
            int user_id;
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "t_jkn_event (event_type, db_key, db_int, event_status," +
                    " try_count, trigger_date) VALUES (13,?,0,0,0,now())");
            while (resultSet.next()){
                user_id = resultSet.getInt("user_id");
                preparedStatement.setInt(1, user_id);
                preparedStatement.executeUpdate();
                System.out.println("insert into  " +  user_id);
            }
        }
    }
}

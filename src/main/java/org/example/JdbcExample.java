import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcExample {

    public static void main(String[] args) {
        String cls = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/souvenirs";
        String un  = "root";
        String  psw = "root";
        try {
            Class.forName(cls);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JdbcExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection conn = null;
        try {
            conn =(Connection) DriverManager.getConnection(url,un, psw);
        } catch (SQLException ex) {
            Logger.getLogger(JdbcExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        String selectTableSQL = "SELECT * from manufacturers";
        Statement stmnt;
        ResultSet rs;
        try {
            stmnt= (Statement) conn.createStatement();
            rs=stmnt.executeQuery(selectTableSQL);
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String info = rs.getString("name");
                System.out.print("id: " + id);
                System.out.println(", info: " + info);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
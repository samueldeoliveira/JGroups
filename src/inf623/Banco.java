package inf623;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Banco {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection con = DriverManager.getConnection("jdbc:mysql://10.70.9.61/banco", "root", "123456")){
            try (PreparedStatement ps = con.prepareStatement("select id, nome, email from Usuario")){
                try (ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        int id = rs.getInt("id");
                        String nome = rs.getString("nome");
                        String email = rs.getString("email");
                        System.out.println(String.format("ID: %d, Nome: %s, E-mail: %s", id, nome, email));
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement("insert into Usuario (nome, email) values (?, ?)")){
                ps.setString(1, "Samuel Lima de Oliveira Junior");
                ps.setString(2, "samueljr@mpf.mp.br");
                ps.executeUpdate();
            }
        }
	}
}
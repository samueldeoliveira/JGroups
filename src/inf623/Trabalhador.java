package inf623;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

//import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Util;

public class Trabalhador extends ReceiverAdapter {
	JChannel channel;
	String user_name = System.getProperty("host.name", "n/a");
	final List<Comando> state = new LinkedList<Comando>();
	
	private void start() throws Exception {
		try {
			channel = new JChannel();
			channel.connect(Constantes.CLUSTER_NAME);
			channel.setReceiver(this);
			channel.getState(null, 10000);
			stopSlave();
		} catch (Exception e) {
			throw new RuntimeException("Falha no metodo start", e);
		} finally {
			channel.close();
		}
	}
	
	private void stopSlave() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.println("Trabalhador");
				System.out.println(String.format("Numero de Mensagens: %d", state.size() ));
				System.out.println(" ");
				System.out.flush();
				String line = in.readLine().toLowerCase();
				if (line.startsWith("quit") || line.startsWith("exit")){
					channel.close();
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	@Override
	public void receive(Message msg) {
		//Metodo chamado ao receber uma mensagem
		Comando mensagem = (Comando)msg.getObject();
		synchronized(state) {
	        state.add(mensagem);
	    }
		System.out.println("Trabalhador RECEIVE "+msg.getSrc()+ ": " + mensagem);
		System.out.println("Trabalhador");
		System.out.println(String.format("Numero de Mensagens: %d", state.size() ));
		System.out.println(" ");
	}
	

	@SuppressWarnings("unchecked")
	public void setState(InputStream input) throws Exception {
	    List<Comando> list;
	    list=(List<Comando>)Util.objectFromStream(new DataInputStream(input));
	    synchronized(state) {
	        state.clear();
	        state.addAll(list);
	    }
	    System.out.println(list.size() + " mensagens no Coordenador:");
	    for(Comando str: list) {
	        System.out.println(str);
	    }
	}

	public static void main(String[] args) throws Exception {
		new Trabalhador().start();
	}

/*
	public static void Banco() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection con = DriverManager.getConnection("jdbc:mysql://192.168.182.31/gate", "????", "????"))
        {
            try (PreparedStatement ps = con.prepareStatement("select id, nome from Usuario"))
            {
                try (ResultSet rs = ps.executeQuery()) 
                {
                    while (rs.next())
                    {
                        int id = rs.getInt("id");
                        String nome = rs.getString("nome");
                        System.out.println(String.format("ID: %d, Nome: %s", id, nome));
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement("insert into Uzer (name, email, Role$id, userID, passwd) values (?, ?, ?, ?, MD5(?))"))
            {
                ps.setString(1, "Samuel Lima de Oliveira Junior");
                ps.setString(2, "samueljr@mpf.mp.br");
                ps.setInt(3, 2);
                ps.setString(4, "samueljr");
                ps.setString(5, "samueljr");
                ps.executeUpdate();
            }
        }
    }	
	
*/

}


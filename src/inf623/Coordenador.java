package inf623;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

public class Coordenador extends ReceiverAdapter {
	JChannel channel;
	String user_name = System.getProperty("user.name", "n/a");
	final List<Comando> state = new LinkedList<Comando>();
	
	
	private void start() throws Exception {
		try {
			channel = new JChannel();
			channel.connect(Constantes.CLUSTER_NAME);
			channel.setReceiver(this);
			eventLoop();
		} catch (Exception e) {
			throw new RuntimeException("Falha no metodo start", e);
		} finally {
			channel.close();
		}
	}

	private void eventLoop() {
		Address addressDest = null;
		Address ownAddres = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.println("Coordenador");
				System.out.println(String.format("Numero de Mensagens: %d", state.size() ));
				System.out.println(" ");
				System.out.print("Digite uma Mensagem >");
				System.out.flush();
				String line = in.readLine().toLowerCase();
				if (line.startsWith("quit") || line.startsWith("exit"))
					break;
				//line = "[" + user_name + "] " + line;
				Message msg = new Message(addressDest, ownAddres, new Comando());
				channel.send(msg);
			} catch (Exception e) {
				// handle exception
			}

		}
	}

	
	@Override
	public void getState(OutputStream output) throws Exception {
	    synchronized(state) {
	        Util.objectToStream(state, new DataOutputStream(output));
	    }
	}
	
	@Override
	public void receive(Message msg) {
		Comando mensagem = (Comando)msg.getObject();
		System.out.println("Coordenador RECEIVE "+msg.getSrc()+ ": " + mensagem);
		synchronized(state) {
	        state.add(mensagem);
	    }
		System.out.println("Coordenador");
		System.out.println(String.format("Numero de Mensagens: %d", state.size() ));
		System.out.println(" ");
	}
	
	@Override
	public void viewAccepted(View view) {
		//Metodo chamado quando um Trabalhador entra ou sai do Grupo
		System.out.println("Composicao do Cluster: "+ view);
		System.out.println("Quantidade de Nohs do cluster: "+ view.size());
		super.viewAccepted(view);
	}

	public static void main(String[] args) throws Exception {
		new Coordenador().start();
	}
}
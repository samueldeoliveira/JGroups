package inf623;

import java.io.Serializable;

public class Comando implements Serializable{
	private static final long serialVersionUID = 2475894039943294090L;
	private Integer sequencia;
	private String query;

	@Override
	public String toString(){
		return "Sequencia [" + sequencia + "] Comando [" + query +"]";
	}
}

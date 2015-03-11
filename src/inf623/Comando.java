package inf623;

import java.io.Serializable;

public class Comando implements Serializable{
	private Integer sequencia;
	private String query;

	@Override
	public String toString(){
		return "Eu sou um comando lindo";
	}
}

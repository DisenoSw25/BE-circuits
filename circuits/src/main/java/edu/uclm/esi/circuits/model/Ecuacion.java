package edu.uclm.esi.circuits.model;


import java.util.ArrayList;
import java.util.List;

public class Ecuacion{
	private List<Simple> sumandos;
	private int lambda;

	public Ecuacion(String eq) {

	    this.sumandos = new ArrayList<>();
	    String[] tokens = eq.split("(?=[-+])");

	    for(String token :tokens) {
	    	int factor = 0;
	    	
	    	Simple sumando = new Simple();
	    	
			if (token.contains("x")) {
				String[] partes = token.split("x");
				factor = Integer.parseInt(partes[0]);	
				sumando.setIndex(Integer.parseInt(partes[1]));
			}
	    	else {
	    		factor = Integer.parseInt(token);
	    		sumando.setIndependiente();
	    	}
	    	
	    	sumando.setFactor(factor);
	    	this.add(sumando);
	    }
	}
	
	public List<Simple> getSumandos() {
		return sumandos;
	}

	public void add(Simple sumando) {
		this.sumandos.add(sumando);

	}

	public int calcular(List<Integer> x) {

		int result = 0;
		
		for (int i = 0; i < this.sumandos.size(); i++)
			result = result + this.sumandos.get(i).calcular(x.get(i));
		return result;

	}
	
	public boolean tieneIndependiente() {
		boolean result = false;
		for (Simple sumando : this.sumandos)
			if (sumando.esIndependiente())
				result = true;
		return result;
	}

	public void setLambda(int lambda) {
			this.lambda = lambda;
		}

	public int getLambda() {
		return this.lambda;
	}

}

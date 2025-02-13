package edu.uclm.esi.circuits.model;

public abstract class Sumando {

	protected int factor;
	protected int index;
	protected boolean independiente = false; //Cuando un termino no tenga x, se activa este, 
											// para que a la hora de calcular, no se mezcle con lo factores.
	
	public void setFactor(int factor) {
		this.factor = factor;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getFactor() {
		return this.factor;
	}

	public int getIndex() {
		return this.index;
	}
	
	public boolean esIndependiente() {
		return this.independiente;
	}
	
	public void setIndependiente() {
		this.independiente = true;
	}
	
	public String getTipo() {
		return this.getClass().toString();
	}

	public int calcular(int x) {
		return this.factor * x;
	}

}
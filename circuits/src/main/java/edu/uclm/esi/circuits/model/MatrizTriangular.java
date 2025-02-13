package edu.uclm.esi.circuits.model;

import java.util.List;

public class MatrizTriangular {
    private int[][] matriz;

    public MatrizTriangular(int n) {
        this.matriz = new int[n][n];
        for (int i = 0; i < n; i++) {
            this.matriz[i] = new int[i + 1];
        }
    }

    public void set(int i, int j, int value) {
        if (esValido(i, j)) {
            matriz[i][j] = value;
        } else {
            throw new IllegalArgumentException("Índices inválidos para una matriz triangular.");
        }
    }

    public int get(int i, int j) {
        if (esValido(i, j)) {
            return matriz[i][j];
        } else {
            throw new IllegalArgumentException("Índices inválidos para una matriz triangular.");
        }
    }
    
    public int [][] GetMatriz(){
    	return matriz;
    }
    
    public int getDimension() {
        return matriz.length;
    }

    private boolean esValido(int i, int j) {
        return i >= 0 && i < matriz.length && j >= 0;
    }
    
    public static MatrizTriangular generarMatrizTriangular(List<Integer> datos) {
        int n = (int) Math.sqrt(2 * datos.size());
        MatrizTriangular matrizTriangular = new MatrizTriangular(n);

        int contador = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n ; j++) {
                matrizTriangular.set(j, i, datos.get(contador));
                contador++;
            }
        }
        
        return matrizTriangular;
    }

    // Otros métodos de utilidad pueden ser agregados según sea necesario
}
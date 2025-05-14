package edu.uclm.esi.circuits.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Circuit {

    @Id @Column(length = 36)
    private String id;
    private int outputQbits;
    @Transient
    private int[][] table;
    @Column(length = 50)
    private String name;
    
    public Circuit() {
        this.id = UUID.randomUUID().toString();
    }

    public String generateCode(String code) {
        int outputQubits = this.getOutputQubits();
        int totalQubits = this.getQubits();
        int inputQubits = totalQubits - outputQubits;

        // #QUBITS#
        code = code.replace("#QUBITS#", String.valueOf(totalQubits));

        // #INITIALIZE#
        StringBuilder initialize = new StringBuilder();
        initialize.append("#Input qubits initialization:\n");
        for (int i = 0; i < inputQubits; i++)
            initialize.append("circuit.initialize(ZERO, ").append(i).append(")\n");
        initialize.append("#Output qubits MUST BE set to 0\n");
        for (int i = inputQubits; i < totalQubits; i++)
            initialize.append("circuit.initialize(ZERO, ").append(i).append(")\n");
        code = code.replace("#INITIALIZE#", initialize.toString());

        // #CALCULUS# (Optimizado)
        StringBuilder calculus = new StringBuilder();
        // Para cada qubit de salida
        for (int out = 0; out < outputQubits; out++) {
            int outputIdx = inputQubits + out;
            // Para cada combinación de entrada que activa este output
            for (int[] values : this.table) {
                if (values[outputIdx] == 1) {
                    // Solo usamos los qubits de entrada relevantes (los que cambian entre filas)
                    StringBuilder pre = new StringBuilder();
                    StringBuilder post = new StringBuilder();
                    StringBuilder controls = new StringBuilder("[");
                    int numControls = 0;
                    for (int in = 0; in < inputQubits; in++) {
                        // Solo añadimos controles para los qubits que son relevantes para esta fila
                        if (values[in] == 0) {
                            pre.append("circuit.x(").append(in).append(")\n");
                            post.append("circuit.x(").append(in).append(")\n");
                        }
                        controls.append(in);
                        if (in < inputQubits - 1) controls.append(", ");
                        numControls++;
                    }
                    controls.append("]");
                    calculus.append(pre);
                    if (numControls > 1) {
                        calculus.append("circuit.mcx(").append(controls).append(", ").append(outputIdx).append(")\n");
                    } else {
                        calculus.append("circuit.cx(").append(controls.substring(1, controls.length()-1)).append(", ").append(outputIdx).append(")\n");
                    }
                    calculus.append(post);
                    calculus.append("circuit.barrier()\n");
                }
            }
        }
        code = code.replace("#CALCULUS#", calculus.toString());
        return code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOutputQubits(int outputQubits) {
        this.outputQbits = outputQubits;
        // System.out.println("Setting outputQubits to " + outputQubits);
    }

    public void setTable(int[][] table) {
        this.table = table;
        // System.out.println("Setting table to " + Arrays.toString(table));
    }

    public int getOutputQubits() {
        return this.outputQbits;
    }

    public int[][] getTable() {
        return table;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getQubits() {
        return this.table[0].length;
    }
}

package edu.uclm.esi.circuits.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.circuits.dao.CircuitDAO;
import edu.uclm.esi.circuits.model.Circuit;

@Service
public class CircuitService {

    @Autowired
    private CircuitDAO circuitDAO;
    
    public String createCircuit(Map<String, Object> body) {
        return "Hola";
    }

    public Map<String, Object> generateCode(Circuit circuit, String token) throws Exception {
        if(circuit.getQubits() > 6) {
            if (token == null)
                throw new Exception("El servicio solicitado requiere pago");
            ProxyBEUsuarios.get().checkCredit(token);
        }

        String templateCode = this.readFile("templates/ibm.local.txt");

        String code =  circuit.generateCode(templateCode);
        if (circuit.getName() != null) 
            this.circuitDAO.save(circuit);
        
        Map<String,Object> result = new HashMap<>();
        result.put("code", code);
        return result;
    }

    private String readFile(String fileName) throws Exception{
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream fis = classLoader.getResourceAsStream(fileName)){
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String s = new String(b);
            return s;
        }
    }
}




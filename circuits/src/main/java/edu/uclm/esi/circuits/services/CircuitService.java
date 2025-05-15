package edu.uclm.esi.circuits.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.circuits.dao.CircuitDAO;
import edu.uclm.esi.circuits.model.Circuit;

@Service
public class CircuitService {

    @Autowired
    private CircuitDAO circuitDAO;

    @Value("${circuits.template.path}")
    private String templatePath;

    @Value("${circuits.max-qubits}")
    private int maxQubits;
    
    public String createCircuit(Map<String, Object> body) {
        return "Hola";
    }

    public Map<String, Object> generateCode(Circuit circuit, String token) throws Exception {
        if(circuit.getQubits() > maxQubits) {
            if (token == null)
                throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, 
                "El servicio solicitado requiere pago");
            ProxyBEUsuarios.get().checkCredit(token);
        }

        String templateCode = this.readFile(templatePath);

        String code =  circuit.generateCode(templateCode);
        circuit.setGeneratedCode(code);
        if (circuit.getName() == null) 
            circuit.setName("Circuit" + circuit.getId());
        this.circuitDAO.save(circuit);
        
        Map<String,Object> result = new HashMap<>();
        result.put("code", code);
        return result;
    }

    public Circuit getCircuitById(String id) {
        return circuitDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Circuito no encontrado"));
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




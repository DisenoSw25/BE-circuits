package edu.uclm.esi.circuits.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        //if(circuit.getQubits() > maxQubits)
            this.circuitDAO.save(circuit); // Poner comprobación de qubits para almacenar
        
        Map<String,Object> result = new HashMap<>();
        result.put("code", code);
        return result;
    }

    public Circuit getCircuitById(String id) {
        return circuitDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Circuito no encontrado"));
    }

    public List<Map<String, String>> getCircuitList() {
        List<Circuit> circuits = circuitDAO.findAll();
        return circuits.stream()
            .map(c -> {
                Map<String, String> map = new HashMap<>();
                map.put("id", c.getId());
                map.put("name", c.getName());
                return map;
            })  // Source: https://github.com/SoonPoong-Hong/hong-boot-netty-public/tree/86d6c8ed451d8f08518b4d230453929600e3bbfd/src/main/java/rocklike/boot/netty/common/ClientCoordinator.java
            .collect(Collectors.toList());
    }

    private String readFile(String fileName) throws Exception{
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream fis = classLoader.getResourceAsStream(fileName)){
            if (fis == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el archivo: " + fileName);
            }
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String s = new String(b);
            return s;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el archivo", e);
        }
    }
}




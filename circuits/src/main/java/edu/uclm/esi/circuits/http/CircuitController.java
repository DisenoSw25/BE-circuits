package edu.uclm.esi.circuits.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.circuits.model.Circuit;
import edu.uclm.esi.circuits.services.CircuitService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("circuits")
@CrossOrigin("*")
public class CircuitController {

    @Autowired
    private CircuitService service;

    @Value("${circuits.tokenGenerateCode}")
    private String tokenGenerateCode;

    @PostMapping("/createCircuit") // No tiene por qué tener el mismo nombre que el método
    public String createCircuit(@RequestBody Map<String, Object> body) {
        if (!body.containsKey("table") || !body.containsKey("outputQubits")){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, 
            "The request body must contain the qubits and outputQubits fields");
        }
       return this.service.createCircuit(body);
    }

    // http..../.../generateCode?name=prueba
    @PostMapping("/generateCode") // Tiene que tener un postmapping único
    public Map<String, Object> generateCode(HttpServletRequest request, @RequestParam (required = false) String name, @RequestBody Circuit circuit) {
        if (name != null)
            circuit.setName(name);
        String token = request.getHeader(tokenGenerateCode);
        try {
            return this.service.generateCode(circuit, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, e.getMessage());
        }
    }
}

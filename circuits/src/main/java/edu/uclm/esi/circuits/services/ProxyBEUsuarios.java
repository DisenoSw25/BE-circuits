package edu.uclm.esi.circuits.services;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;

public class ProxyBEUsuarios {
    private static ProxyBEUsuarios yo;

    @Value("${circuits.usuarios.url}")
    private String urlUsuarios;

    @Value("${circuits.usuarios.checkToken}")
    private String checkTokenExtensionURL;

    // Constructor privado
    private ProxyBEUsuarios() {
    }

    public void checkCredit(String token) throws Exception{
        HttpGet httpGet = new HttpGet(this.urlUsuarios + checkTokenExtensionURL + token);
        try(CloseableHttpClient httpClient = HttpClients.createDefault()){
            try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
            int code = response.getCode();
            if (code != 200)
                throw new Exception("El servicio solicitado requiere pago");
            }
        }
    }

    public static ProxyBEUsuarios get() {
        if (yo == null)
            yo = new ProxyBEUsuarios();
        return yo;
    }
}

package dev.marco.taskapi.web.config.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretGenerator {

	public static void main(String[] args) {
		//byte[] key = new byte[64]; crea un algoritmo de 512 bits
		byte[] key = new byte[32]; // crea un algoritmo de 256 bits
        new SecureRandom().nextBytes(key);
        String base64 = Base64.getEncoder().encodeToString(key);
        System.out.println(base64);
	}
}

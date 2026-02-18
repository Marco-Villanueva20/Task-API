package dev.marco.taskapi.web.config.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dev.marco.taskapi.domain.provider.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService implements TokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	private final SecretKey signingKey;
	private final long jwtExpiration;

	public JwtService(@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration}") long expiration) {
		this.jwtExpiration = expiration;
		this.signingKey = buildKey(secret);
	}

	private SecretKey buildKey(String secret) {
		if (secret == null || secret.isBlank()) {
			logger.warn("JWT Secret no provisto. Generando clave aleatoria (Solo Desarrollo)");
			return Jwts.SIG.HS256.key().build();
		}
		// Decodificacion Base64 (MAs seguro que .getBytes())
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// --- GENERACIÓN ---

	@Override
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	// Flexibilidad para incluir Roles o Permisos (Claims adicionales)
	public String generateToken(Map<String, Object> extraClaims,
			UserDetails userDetails) {
		return Jwts.builder()
				.claims(extraClaims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(signingKey).compact();
	}

	// --- EXTRACCIÓN ---

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Generico para extraer cualquier dato (ej: Roles)
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
	}

	// --- VALIDACIÓN ---

	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		} catch (JwtException | IllegalArgumentException e) {
			logger.error("Token JWT inválido o corrupto: {}", e.getMessage());
			return false;
		}
	}
	
	public boolean isTokenSignatureValid(String token) {
	    try {
	        Jwts.parser()
	            .verifyWith(signingKey)
	            .build()
	            .parseSignedClaims(token);
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        logger.error("Firma o estructura de JWT inválida: {}", e.getMessage());
	        return false;
	    }
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}

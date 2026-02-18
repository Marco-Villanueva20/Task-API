package dev.marco.taskapi.domain.provider;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {
	String generateToken(UserDetails userDetails);
}

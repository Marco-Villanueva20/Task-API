package dev.marco.taskapi.web.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.marco.taskapi.web.config.jwt.JwtService;

import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor // Lo mejor del 1
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		try {
	        String jwt = parseJwt(request);

	        if (jwt != null) {
	            // Si la firma es falsa o expiro, extractUsername lanzara JwtException
	            String username = jwtService.extractUsername(jwt);

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	                // 2. Aqui  solo valida que el usuario no haya sido deshabilitado
	                // o que el token no haya expirado
	                if (jwtService.isTokenValid(jwt, userDetails)) {
	                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                            userDetails, null, userDetails.getAuthorities());
	                    
	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                }
	            }
	        }
	    } catch (Exception e) {
	        // Si el token era invalido, llegara aqui. Logueamos y seguimos.
	        log.error("Fallo la autenticación: {}", e.getMessage());
	    }
	    filterChain.doFilter(request, response);
	}

	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
}
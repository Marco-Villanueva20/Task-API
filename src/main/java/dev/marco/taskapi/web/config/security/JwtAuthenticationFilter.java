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

@Component
@RequiredArgsConstructor // Lo mejor del 1
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		try {
			String jwt = parseJwt(request);

			// ¿Existe el token y la firma es valida?
			// (No vamos a la DB todavia)
			if (jwt != null && jwtService.isTokenSignatureValid(jwt)) {
				String username = jwtService.extractUsername(jwt);

				// Si hay usuario y no esta autenticado en el contexto
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					
					// vamos a la DB
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					// Validacion final (Nombre coincide y no expiro)
					if (jwtService.isTokenValid(jwt, userDetails)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}

				}
			}
		} catch (Exception e) {
			// Logueamos pero no detenemos la cadena
			logger.error("No se pudo establecer la autenticación de usuario", e);
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
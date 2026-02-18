package dev.marco.taskapi.domain.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.marco.taskapi.persistence.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	
	private final UserJpaRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

		/*
		 * Se podria devolver un User pero el UserEntity ya implementa UserDetails, por
		 * lo que se puede devolver directamente el UserEntity return
		 * User.withUsername(u.getUsername()) .password(u.getPassword())
		 * .authorities(Collections.emptyList()) .build();
		 * 
		 */
	}
}

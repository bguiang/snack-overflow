package com.bernardguiang.SnackOverflow.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

@Service
public class ApplicationUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	public ApplicationUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername((username));
		user.orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
		return new ApplicationUserDetails(user.get());
	}
}

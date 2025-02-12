package com.coinMarket.controller;

import com.coinMarket.configuration.JwtProvider;
import com.coinMarket.model.User;
import com.coinMarket.repositories.IUserRepository;
import com.coinMarket.response.AuthResponse;
import com.coinMarket.service.CustomUserDetailsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

	IUserRepository userRepository;
	CustomUserDetailsService customUserDetailsService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody User user) {

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
		}

		User newUser = User.builder()
			  .fullName(user.getFullName())
			  .password(user.getPassword())
			  .email(user.getEmail())
			  .build();
		newUser = userRepository.save(newUser);

		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);

		String jwt = JwtProvider.generateToken(auth);

		AuthResponse response = AuthResponse.builder()
			  .jwt(jwt)
			  .status(true)
			  .message("Register success")
			  .build();
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody User user) {
		String username = user.getEmail();
		String password = user.getPassword();

		Authentication auth = authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(auth);

		String jwt = JwtProvider.generateToken(auth);

		AuthResponse response = AuthResponse.builder()
			  .jwt(jwt)
			  .status(true)
			  .message("Login success")
			  .build();
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		if (!password.equals(userDetails.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}
}

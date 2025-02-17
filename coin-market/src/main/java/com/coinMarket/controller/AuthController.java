package com.coinMarket.controller;

import com.coinMarket.configuration.JwtProvider;
import com.coinMarket.model.TwoFactorOTP;
import com.coinMarket.model.User;
import com.coinMarket.model.Watchlist;
import com.coinMarket.repositories.IUserRepository;
import com.coinMarket.response.AuthResponse;
import com.coinMarket.service.CustomUserDetailsService;
import com.coinMarket.service.EmailService;
import com.coinMarket.service.ITwoFactorOtpService;
import com.coinMarket.service.IWatchlistService;
import com.coinMarket.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

	IUserRepository userRepository;
	CustomUserDetailsService customUserDetailsService;
	ITwoFactorOtpService twoFactorOtpService;
	EmailService emailService;
	IWatchlistService watchlistService;

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

		watchlistService.createWatchList(newUser);

		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail()
			  , user.getPassword());
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
	public ResponseEntity<AuthResponse> login(@RequestBody User user) throws MessagingException {
		String username = user.getEmail();
		String password = user.getPassword();

		Authentication auth = authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(auth);

		String jwt = JwtProvider.generateToken(auth);

		User authUser = userRepository.findByEmail(username);

		if (user.getTwoFactorAuth().isEnabled()) {
			AuthResponse res = AuthResponse.builder()
				  .message("two factor auth is enabled")
				  .isTwoFactorAuthEnabled(true)
				  .build();
			String otp = OtpUtil.generateOtp();

			TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUserId(authUser.getId());
			if (oldTwoFactorOTP != null) {
				twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
			}
			TwoFactorOTP newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);
//			======================send email
			emailService.sendEmailVerificationEmail(username, otp);


			res.setSession(newTwoFactorOtp.getId());
			return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
		}

		AuthResponse response = AuthResponse.builder()
			  .jwt(jwt)
			  .status(true)
			  .message("Login success")
			  .build();
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}


	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp, @RequestParam String twoFactorOtpId) throws Exception {
		TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(twoFactorOtpId);
		if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
			AuthResponse response = AuthResponse.builder()
				  .message("Two factor authentication verified")
				  .isTwoFactorAuthEnabled(true)
				  .jwt(twoFactorOTP.getJwt())
				  .build();
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		throw new Exception("invalid otp");
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		if (!password.equals(userDetails.getPassword())) {
			throw new BadCredentialsException("Wrong password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}
}

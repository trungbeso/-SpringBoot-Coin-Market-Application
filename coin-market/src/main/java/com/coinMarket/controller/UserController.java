package com.coinMarket.controller;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.User;
import com.coinMarket.model.VerificationCode;
import com.coinMarket.service.EmailService;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.IVerificationCodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

	IUserService userService;
	EmailService emailService;
	IVerificationCodeService verificationCodeService;

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}


	@PostMapping("/enable-two-factor/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOtp(
		  @PathVariable VerificationType verificationType,
		  @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
		if (verificationCode == null) {
			verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
		}

		if (verificationType.equals(VerificationType.EMAIL)) {
			emailService.sendEmailVerificationEmail(user.getEmail(), verificationCode.getEmail());
		}

		return new ResponseEntity<>("Verification otp send successfully", HttpStatus.OK);
	}

	@PatchMapping("/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
	                                                          @PathVariable String otp) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
		String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
			  ? verificationCode.getEmail() : verificationCode.getMobile();

		boolean isVerified = verificationCode.getOtp().equals(otp);
		if (isVerified) {
			User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),sendTo,user);

			verificationCodeService.deleteVerificationCodeById(verificationCode);
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		}
		throw new Exception("Wrong otp");
	}
}

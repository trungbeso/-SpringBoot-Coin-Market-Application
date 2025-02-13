package com.coinMarket.controller;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.ForgotPasswordToken;
import com.coinMarket.model.User;
import com.coinMarket.model.VerificationCode;
import com.coinMarket.request.ForgotPasswordTokenRequest;
import com.coinMarket.request.ResetPasswordRequest;
import com.coinMarket.response.ApiResponse;
import com.coinMarket.response.AuthResponse;
import com.coinMarket.service.EmailService;
import com.coinMarket.service.IForgotPasswordService;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.IVerificationCodeService;
import com.coinMarket.utils.OtpUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

	IUserService userService;
	EmailService emailService;
	IVerificationCodeService verificationCodeService;
	IForgotPasswordService forgotPasswordService;

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		return new ResponseEntity<>(user, HttpStatus.OK);
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

	@PostMapping("/auth/reset-password/send-otp")
	public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
		  @RequestBody ForgotPasswordTokenRequest request) throws Exception {

		User user = userService.findUserProfileByEmail(request.getSendTo());
		String otp = OtpUtil.generateOtp();
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();

		ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
		if (token == null) {
			token = forgotPasswordService.createToken(user, id, otp,request.getVerificationType() ,request.getSendTo());
		}

		if (request.getVerificationType().equals(VerificationType.EMAIL)) {
			emailService.sendEmailVerificationEmail(user.getEmail(),token.getOtp());
		}
		AuthResponse response = AuthResponse.builder()
			  .session(token.getId())
			  .message("Password reset otp send")
			  .build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/auth/reset-password/verify-otp")
	public ResponseEntity<ApiResponse> resetPassword(@RequestHeader("Authorization") String jwt,
	                                          @RequestBody ResetPasswordRequest request,
	                                          @RequestParam String id) throws Exception {
		ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

		boolean isVerified = forgotPasswordToken.getOtp().equals(request.getOtp());
		if (isVerified) {
			userService.updatePassword(forgotPasswordToken.getUser(), request.getPassword());
			ApiResponse response = new ApiResponse();
			response.setMessage("Password update successfully");
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}
		throw new Exception("Wrong otp");
	}
}

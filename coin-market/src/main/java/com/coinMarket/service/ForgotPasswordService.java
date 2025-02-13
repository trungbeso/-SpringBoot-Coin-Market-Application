package com.coinMarket.service;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.ForgotPasswordToken;
import com.coinMarket.model.User;
import com.coinMarket.repositories.IForgotPasswordRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ForgotPasswordService implements IForgotPasswordService{

	IForgotPasswordRepository forgotPasswordRepository;

	@Override
	public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType type, String sendTo) {
		ForgotPasswordToken token = new ForgotPasswordToken();
		token.setUser(user);
		token.setSendTo(sendTo);
		token.setOtp(otp);
		token.setVerificationType(type);
		token = forgotPasswordRepository.save(token);

		return token;
	}

	@Override
	public ForgotPasswordToken findById(String id) {
		Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);
		return token.orElse(null);
	}

	@Override
	public ForgotPasswordToken findByUser(Long userId) {
		return forgotPasswordRepository.findByUserId(userId);
	}

	@Override
	public void deleteToken(ForgotPasswordToken token) {
		forgotPasswordRepository.delete(token);
	}
}

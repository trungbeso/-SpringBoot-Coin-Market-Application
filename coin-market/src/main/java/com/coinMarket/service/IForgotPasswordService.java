package com.coinMarket.service;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.ForgotPasswordToken;
import com.coinMarket.model.User;

public interface IForgotPasswordService {
	ForgotPasswordToken createToken(User user, String id, String otp, VerificationType type, String sendTo);

	ForgotPasswordToken findById(String id);

	ForgotPasswordToken findByUser(Long userId);

	void deleteToken(ForgotPasswordToken token);
}

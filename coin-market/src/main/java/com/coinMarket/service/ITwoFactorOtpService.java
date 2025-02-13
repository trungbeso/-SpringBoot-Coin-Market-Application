package com.coinMarket.service;

import com.coinMarket.model.TwoFactorAuth;
import com.coinMarket.model.TwoFactorOTP;
import com.coinMarket.model.User;

public interface ITwoFactorOtpService {
	TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

	TwoFactorOTP findByUserId(Long userId);

	TwoFactorOTP findById (String id);

	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);

	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}

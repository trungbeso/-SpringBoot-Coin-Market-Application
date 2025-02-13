package com.coinMarket.service;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.User;
import com.coinMarket.model.VerificationCode;

public interface IVerificationCodeService {
	VerificationCode sendVerificationCode(User user, VerificationType type);

	VerificationCode getVerificationCodeById(Long id) throws Exception;

	VerificationCode getVerificationCodeByUserId(Long userId);

	void deleteVerificationCodeById(VerificationCode verificationCode);
}

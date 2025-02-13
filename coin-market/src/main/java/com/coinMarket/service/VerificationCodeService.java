package com.coinMarket.service;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.User;
import com.coinMarket.model.VerificationCode;
import com.coinMarket.repositories.IVerificationCodeRepository;
import com.coinMarket.utils.OtpUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VerificationCodeService implements IVerificationCodeService{

	IVerificationCodeRepository verificationCodeRepository;

	@Override
	public VerificationCode sendVerificationCode(User user, VerificationType type) {
		VerificationCode code1 = new VerificationCode();
		code1.setOtp(OtpUtil.generateOtp());
		code1.setVerificationType(type);
		code1.setUser(user);

		code1 = verificationCodeRepository.save(code1);
		return code1;
	}

	@Override
	public VerificationCode getVerificationCodeById(Long id) throws Exception {
		Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
		if (verificationCode.isPresent()) {
			return verificationCode.get();
		}
		throw new Exception("Verification code not found");
	}

	@Override
	public VerificationCode getVerificationCodeByUserId(Long userId) {
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerificationCode verificationCode) {
		verificationCodeRepository.delete(verificationCode);
	}
}

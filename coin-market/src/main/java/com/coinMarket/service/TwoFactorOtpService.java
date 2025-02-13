package com.coinMarket.service;

import com.coinMarket.model.TwoFactorAuth;
import com.coinMarket.model.TwoFactorOTP;
import com.coinMarket.model.User;
import com.coinMarket.repositories.ITwoFactorOtpRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class TwoFactorOtpService implements ITwoFactorOtpService{

	ITwoFactorOtpRepository otpRepository;

	@Override
	public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();

		String id = uuid.toString();

		TwoFactorOTP twoFactorOTP = TwoFactorOTP.builder()
			  .otp(otp)
			  .jwt(jwt)
			  .user(user)
			  .id(id)
			  .user(user)
			  .build();
		twoFactorOTP = otpRepository.save(twoFactorOTP);

		return twoFactorOTP;
	}

	@Override
	public TwoFactorOTP findByUserId(Long userId) {
		return otpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOTP findById(String id) {
		Optional<TwoFactorOTP> otpOptional = otpRepository.findById(id);

		return otpOptional.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
		otpRepository.delete(twoFactorOtp);
	}
}

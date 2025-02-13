package com.coinMarket.service;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.User;

public interface IUserService {
	User findUserProfileByJwt(String jwt) throws Exception;

	User findUserProfileByEmail(String email) throws Exception;

	User findUserById(Long id) throws Exception;

	User enableTwoFactorAuthentication(VerificationType type, String sendTo, User user);

	User updatePassword(User user, String newPassword);
}

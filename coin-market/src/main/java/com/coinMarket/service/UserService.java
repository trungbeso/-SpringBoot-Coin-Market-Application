package com.coinMarket.service;

import com.coinMarket.configuration.JwtProvider;
import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.TwoFactorAuth;
import com.coinMarket.model.User;
import com.coinMarket.repositories.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

	IUserRepository userRepository;

	@Override
	public User findUserProfileByJwt(String jwt) throws Exception {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new Exception("user not found");
		}
		return user;
	}

	@Override
	public User findUserProfileByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new Exception("user not found");
		}
		return user;
	}

	@Override
	public User findUserById(Long id) throws Exception {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new Exception("User not found");
		}
		return user.get();
	}

	@Override
	public User enableTwoFactorAuthentication(VerificationType type, String sendTo, User user) {
		TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
		twoFactorAuth.setEnabled(true);
		twoFactorAuth.setSendTo(type);

		user.setTwoFactorAuth(twoFactorAuth);
		user = userRepository.save(user);

		return user;
	}

	@Override
	public User updatePassword(User user, String newPassword) {
		user.setPassword(newPassword);
		user = userRepository.save(user);
		return user;
	}
}

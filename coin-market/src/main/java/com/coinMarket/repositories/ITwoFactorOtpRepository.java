package com.coinMarket.repositories;

import com.coinMarket.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
	TwoFactorOTP findByUserId(Long userId);

}

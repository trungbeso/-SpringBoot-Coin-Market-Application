package com.coinMarket.repositories;

import com.coinMarket.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
	ForgotPasswordToken findByUserId(Long userId);
}

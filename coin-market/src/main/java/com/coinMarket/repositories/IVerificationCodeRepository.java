package com.coinMarket.repositories;

import com.coinMarket.enums.VerificationType;
import com.coinMarket.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
	 VerificationCode findByUserId(Long userId);

}

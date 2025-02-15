package com.coinMarket.repositories;

import com.coinMarket.model.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
	PaymentDetail findByUserId(Long userId);
}

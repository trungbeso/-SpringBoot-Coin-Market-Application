package com.coinMarket.repositories;

import com.coinMarket.model.PaymentOrder;
import com.paypal.api.payments.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}

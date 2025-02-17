package com.coinMarket.service;

import com.coinMarket.enums.PaymentMethod;
import com.coinMarket.model.PaymentOrder;
import com.coinMarket.model.User;
import com.coinMarket.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface IPaymentOrderService {
	PaymentOrder createOrder (User user, Long amount,
	                          PaymentMethod method);

	PaymentOrder getPaymentOrderById(Long id);

	Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

	PaymentResponse createRazorpayPaymentLing(User user, Long amount, Long orderId) throws RazorpayException;

	PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId) throws StripeException;
}

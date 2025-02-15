package com.coinMarket.service;

import com.coinMarket.enums.PaymentMethod;
import com.coinMarket.enums.PaymentOrderStatus;
import com.coinMarket.model.PaymentOrder;
import com.coinMarket.model.User;
import com.coinMarket.repositories.IPaymentOrderRepository;
import com.coinMarket.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.antlr.v4.runtime.misc.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentOrderService implements IPaymentOrderService{

	IPaymentOrderRepository paymentOrderRepository;

	@NonFinal
	@Value("${razorpay.api.key}")
	String apiKey;

	@NonFinal
	@Value("${stripe.api.key}")
	String stripeSecretKey;

	@NonFinal
	@Value("${razorpay.secret.key}")
	String apiSecretKey;

	@Override
	public PaymentOrder createOrder(User user, Long amount, PaymentMethod method) {
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setUser(user);
		paymentOrder.setAmount(amount);
		paymentOrder.setMethod(method);
		paymentOrder = paymentOrderRepository.save(paymentOrder);
		return paymentOrder;
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long id) {
		return paymentOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment order not found"));
	}

	@Override
	public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
		if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			if (paymentOrder.getMethod().equals(PaymentMethod.RAZORPAY)) {
				RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);
				Payment payment = razorpay.payments.fetch(paymentId);

				Integer amount = payment.get("amount");
				String status = payment.get("status");

				if (status.equals("captured")) {
					paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
					return true;
				}
				paymentOrder.setStatus(PaymentOrderStatus.FAILED);
				paymentOrderRepository.save(paymentOrder);
				return false;
			}
			paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
			paymentOrderRepository.save(paymentOrder);
			return true;
		}
		return false;
	}

	@Override
	public PaymentResponse createRazorpayPaymentLing(User user, Long amount) throws RazorpayException {
		Long Amount = amount * 100;
		try {
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", Amount);
			paymentLinkRequest.put("currency", "USD");

			JSONObject customer = new JSONObject();
			customer.put("name", user.getFullName());
			customer.put("email", user.getEmail());

			paymentLinkRequest.put("customer", customer);
			//create a JSON object with the notification settings
			JSONObject notify = new JSONObject();
			notify.put("email", true);

			paymentLinkRequest.put("notify", notify);

			//set reminder settings
			paymentLinkRequest.put("reminder_enable", true);

			//set callback url and method
			paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
			paymentLinkRequest.put("callback_method", "get");

			//create the payment link using the paymentLink.create() method
			PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

			String paymentLinkId = paymentLink.get("id");
			String paymentLinkUrl = paymentLink.get("short_url");

			PaymentResponse response = new PaymentResponse(paymentLinkUrl);

			return response;
		} catch (RazorpayException e) {
			System.out.println("Error: " + e.getMessage());
			throw new RazorpayException(e.getMessage());
		}
	}

	@Override
	public PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId) throws StripeException {
		Stripe.apiKey = apiKey;

		SessionCreateParams params = SessionCreateParams.builder()
			  .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
			  .setMode(SessionCreateParams.Mode.PAYMENT)
			  .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId)
			  .setCancelUrl("http://localhost:5173/payment/cancel")
			  .addLineItem(SessionCreateParams.LineItem.builder()
				    .setQuantity(1L)
				    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
					      .setCurrency("usd")
					      .setUnitAmount(amount*100)
					      .setProductData(SessionCreateParams
						        .LineItem
						        .PriceData
						        .ProductData
						        .builder()
						        .setName("Top up Wallet")
						        .build())
					      .build())
				    .build())
			  .build();

		Session session = Session.create(params);
		System.out.println("Session created: " + session);

		PaymentResponse response = new PaymentResponse(session.getUrl());
		return response;
	}
}

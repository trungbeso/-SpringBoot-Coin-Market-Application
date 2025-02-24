package com.coinMarket.controller;

import com.coinMarket.enums.PaymentMethod;
import com.coinMarket.model.PaymentOrder;
import com.coinMarket.model.User;
import com.coinMarket.paypal.IPaypalService;
import com.coinMarket.response.PaymentResponse;
import com.coinMarket.service.IPaymentOrderService;
import com.coinMarket.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/payment")
public class PaymentController {

	IUserService userService;
	IPaymentOrderService paymentService;

	@PostMapping("/{paymentMethod}/amount/{amount}")
	public ResponseEntity<PaymentResponse> paymentHandler(@PathVariable PaymentMethod paymentMethod,
	                                                      @PathVariable Long amount,
	                                                      @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		PaymentResponse paymentResponse;

		PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

		if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
			paymentResponse = paymentService.createRazorpayPaymentLing(user, amount, order.getId());
		} else {
			paymentResponse = paymentService.createStripePaymentLing(user, amount, order.getId());
		}
		return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
	}


}

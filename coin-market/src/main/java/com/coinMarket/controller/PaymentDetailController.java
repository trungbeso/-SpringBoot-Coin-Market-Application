package com.coinMarket.controller;

import com.coinMarket.model.PaymentDetail;
import com.coinMarket.model.User;
import com.coinMarket.service.IPaymentDetailService;
import com.coinMarket.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payment")
public class PaymentDetailController {

	IUserService userService;
	IPaymentDetailService paymentDetailService;

	@PostMapping("/details")
	public ResponseEntity<PaymentDetail> addPaymentDetail(@RequestBody PaymentDetail request, @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		PaymentDetail paymentDetail = paymentDetailService.addPaymentDetail(
			  request.getAccountNumber(),
			  request.getAccountHolderName(),
			  request.getIfsc(),
			  request.getBankName(),
			  user
		);
		return new ResponseEntity<>(paymentDetail, HttpStatus.CREATED);
	}

	@GetMapping("/details")
	public ResponseEntity<PaymentDetail> getUserPaymentDetail(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		PaymentDetail paymentDetail = paymentDetailService.getUserPaymentDetail(user);
		return new ResponseEntity<>(paymentDetail, HttpStatus.CREATED);
	}



}

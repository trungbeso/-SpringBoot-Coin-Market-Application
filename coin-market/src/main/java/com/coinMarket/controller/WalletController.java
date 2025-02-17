package com.coinMarket.controller;

import com.coinMarket.model.*;
import com.coinMarket.service.IPaymentOrderService;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.IWalletService;
import com.coinMarket.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/wallets")
public class WalletController {

	IWalletService walletService;
	IUserService userService;
	OrderService orderService;
	IPaymentOrderService paymentService;


	@GetMapping
	public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Wallet wallet = walletService.getUserWallet(user);

		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{walletId}/transfer")
	public ResponseEntity<Wallet> walletToWalletTransfer(
		  @RequestHeader("Authorization") String jwt,
		  @PathVariable Long walletId,
		  @RequestBody WalletTransaction request) throws Exception {

		User senderUser = userService.findUserProfileByJwt(jwt);
		Wallet receiverWallet = walletService.findById(walletId);
		Wallet wallet = walletService.walletToWalletTransfer(
			  senderUser,
			  receiverWallet,
			  request.getAmount());

		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}

	@PutMapping("/order/{orderId}/pay")
	public ResponseEntity<Wallet> payOrderPayment(
		  @RequestHeader("Authorization") String jwt,
		  @PathVariable Long orderId) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		Wallet wallet = walletService.payOrderPayment(order, user);

		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}

	@PutMapping("/wallet/deposit")
	public ResponseEntity<Wallet> addBalanceToWallet(
		  @RequestHeader("Authorization") String jwt,
		  @RequestParam(name = "order_id") Long orderId,
		  @RequestParam(name = "payment_id") String paymentId) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);

		Wallet wallet = walletService.getUserWallet(user);

		PaymentOrder order = paymentService.getPaymentOrderById(orderId);

		Boolean status = paymentService.ProceedPaymentOrder(order, paymentId);

		if (wallet.getBalance().equals(null)) {
			wallet.setBalance(BigDecimal.ZERO);
		}
		if (status) {
			wallet = walletService.addBalance(wallet, order.getAmount());
		}

		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}


}

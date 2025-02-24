package com.coinMarket.controller;

import com.coinMarket.model.User;
import com.coinMarket.model.Wallet;
import com.coinMarket.model.WalletTransaction;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.IWalletTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/transactions")
public class TransactionController {

	IUserService userService;
	IWalletTransactionService walletTransactionService;

//	@GetMapping
//	public ResponseEntity<List<WalletTransaction>> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
//		User user = userService.findUserProfileByJwt(jwt);
//
//		Wallet wallet = walletTransactionService.getUserWallet(user);
//
//		List<WalletTransaction> transactionList = walletTransactionService.getTransactionByWallet(wallet);
//
//		return new ResponseEntity<>(transactionList, HttpStatus.ACCEPTED);
//
//	}
}

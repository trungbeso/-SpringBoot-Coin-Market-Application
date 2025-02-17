package com.coinMarket.controller;

import com.coinMarket.model.User;
import com.coinMarket.model.Wallet;
import com.coinMarket.model.WalletTransaction;
import com.coinMarket.model.Withdraw;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.IWalletService;
import com.coinMarket.service.IWalletTransactionService;
import com.coinMarket.service.IWithdrawService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("${api.prefix}/withdraws")
public class WithdrawController {
	IWithdrawService withdrawService;
	IWalletService walletService;
	IUserService userService;
	IWalletTransactionService walletTransactionService;

	@PostMapping("/{amount}")
	public ResponseEntity<?> withdrawRequest(@PathVariable Long amount,
	                                         @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Wallet userWallet = walletService.getUserWallet(user);
		Withdraw withdraw = withdrawService.requestWithdraw(amount, user);

		//subtract
		walletService.addBalance(userWallet, -withdraw.getAmount());

//		WalletTransaction walletTransaction = walletTransactionService.createTransaction(user);

		return new ResponseEntity<>(withdraw, HttpStatus.OK);
	}

	@PatchMapping("/admin/{id}/process/{isAccept}")
	public ResponseEntity<?> processWithdraw(@PathVariable Long id,
	                                         @PathVariable boolean isAccept,
	                                         @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Withdraw withdraw = withdrawService.processWithdraw(id, isAccept);

		Wallet userWallet = walletService.getUserWallet(user);
		if (!isAccept) {
			walletService.addBalance(userWallet, withdraw.getAmount());
		}
		return new ResponseEntity<>(withdraw, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Withdraw>> getWithdrawHistory(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Withdraw> withdraws = withdrawService.getUsersWithdrawHistory(user);
		return new ResponseEntity<>(withdraws, HttpStatus.OK);
	}

	@GetMapping("/admin")
	public ResponseEntity<List<Withdraw>> getAllWithdrawRequest(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Withdraw> withdraws = withdrawService.getAllWithdrawRequest();
		return new ResponseEntity<>(withdraws, HttpStatus.OK);
	}

}

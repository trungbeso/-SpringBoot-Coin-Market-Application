package com.coinMarket.service;

import com.coinMarket.model.Order;
import com.coinMarket.model.User;
import com.coinMarket.model.Wallet;

public interface IWalletService {
	Wallet getUserWallet(User user);

	Wallet addBalance(Wallet wallet, Long amount);

	Wallet findById(Long id) throws Exception;

	Wallet walletToWalletTransfer(User transfer, Wallet receiverWallet, Long amount) throws Exception;

	Wallet payOrderPayment(Order order, User user) throws Exception;
}

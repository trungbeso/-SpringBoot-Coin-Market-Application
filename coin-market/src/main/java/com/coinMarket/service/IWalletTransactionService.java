package com.coinMarket.service;

import com.coinMarket.model.User;
import com.coinMarket.model.Wallet;
import com.coinMarket.model.WalletTransaction;

import java.util.List;

public interface IWalletTransactionService {
//	Wallet getUserWallet(User user);

	List<WalletTransaction> getTransactionByWallet (Wallet wallet);
}

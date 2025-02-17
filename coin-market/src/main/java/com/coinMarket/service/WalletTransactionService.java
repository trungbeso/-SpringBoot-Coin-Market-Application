package com.coinMarket.service;

import com.coinMarket.model.User;
import com.coinMarket.model.Wallet;
import com.coinMarket.model.WalletTransaction;
import com.coinMarket.repositories.IWalletTransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WalletTransactionService implements IWalletTransactionService{
	IWalletTransactionRepository walletTransactionRepository;


	@Override
	public Wallet getUserWallet(User user) {
		return walletTransactionRepository.getUserWallet(user).getWallet();
	}

	@Override
	public List<WalletTransaction> getTransactionByWallet(Wallet wallet) {
		return List.of();
	}
}

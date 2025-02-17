package com.coinMarket.service;

import com.coinMarket.enums.OrderType;
import com.coinMarket.model.Order;
import com.coinMarket.model.User;
import com.coinMarket.model.Wallet;
import com.coinMarket.repositories.IWalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WalletService implements IWalletService{

	IWalletRepository walletRepository;

	@Override
	public Wallet getUserWallet(User user) {
		Wallet wallet = walletRepository.findByUserId(user.getId());
		if (wallet == null) {
			wallet = new Wallet();
			wallet.setUser(user);
			wallet = walletRepository.save(wallet);
		}
		return wallet;
	}

	@Override
	public Wallet addBalance(Wallet wallet, Long amount) {
		BigDecimal balance = wallet.getBalance();
		BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));

		wallet.setBalance(newBalance);

		return walletRepository.save(wallet);
	}

	@Override
	public Wallet findById(Long id) throws Exception {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if (wallet.isPresent()) {
			return wallet.get();
		}
		throw new Exception("Wallet not found");
	}

	@Override
	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
		Wallet senderWallet = getUserWallet(sender);
		if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
			throw new Exception("Insufficient funds");
		}
		BigDecimal senderBalance = senderWallet
			  .getBalance()
			  .subtract(BigDecimal.valueOf(amount));
		senderWallet.setBalance(senderBalance);
		walletRepository.save(senderWallet);

		BigDecimal receiverBalance = receiverWallet
			  .getBalance()
			  .add(BigDecimal.valueOf(amount));
		receiverWallet.setBalance(receiverBalance);
		walletRepository.save(receiverWallet);
		return senderWallet;
	}

	@Override
	public Wallet payOrderPayment(Order order, User user) throws Exception {
		Wallet wallet = getUserWallet(user);

		BigDecimal newBalance;
		if (order.getOrderType().equals(OrderType.BUY)) {
			newBalance = wallet.getBalance().subtract(order.getPrice());
			if (newBalance.compareTo(order.getPrice()) < 0) {
				throw new Exception("Insufficient funds for this transaction");
			}
		} else {
			newBalance = wallet.getBalance().add(order.getPrice());
		}
		wallet.setBalance(newBalance);
		wallet = walletRepository.save(wallet);
		return wallet;
	}
}

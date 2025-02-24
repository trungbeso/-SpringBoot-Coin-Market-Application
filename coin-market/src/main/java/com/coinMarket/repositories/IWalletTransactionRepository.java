package com.coinMarket.repositories;

import com.coinMarket.model.User;
import com.coinMarket.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
	//WalletTransaction getUserWallet(User user);
}

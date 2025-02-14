package com.coinMarket.repositories;

import com.coinMarket.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet, Long> {
	Wallet findByUserId(Long userId);
}

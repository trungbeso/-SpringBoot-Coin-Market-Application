package com.coinMarket.repositories;

import com.coinMarket.model.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWithdrawRepository extends JpaRepository<Withdraw, Long> {
	List<Withdraw> findByUserId(Long userId);
}

package com.coinMarket.service;

import com.coinMarket.model.User;
import com.coinMarket.model.Withdraw;

import java.util.List;

public interface IWithdrawService {
	Withdraw requestWithdraw(Long amount, User user);

	Withdraw processWithdraw(Long withdrawId, boolean accept);

	List<Withdraw> getUsersWithdrawHistory(User user);

	List<Withdraw> getAllWithdrawRequest();
}

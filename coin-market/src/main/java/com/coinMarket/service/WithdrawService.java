package com.coinMarket.service;

import com.coinMarket.enums.WithdrawalStatus;
import com.coinMarket.model.User;
import com.coinMarket.model.Withdraw;
import com.coinMarket.repositories.IWithdrawRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WithdrawService implements IWithdrawService {

	IWithdrawRepository withdrawRepository;

	@Override
	public Withdraw requestWithdraw(Long amount, User user) {
		Withdraw withDraw = new Withdraw();
		withDraw.setAmount(amount);
		withDraw.setUser(user);
		withDraw.setStatus(WithdrawalStatus.PENDING);
		withDraw = withdrawRepository.save(withDraw);

		return withDraw;
	}

	@Override
	public Withdraw processWithdraw(Long withdrawId, boolean accept) {
		Optional<Withdraw> withdrawOpt = withdrawRepository.findById(withdrawId);
		if (withdrawOpt.isEmpty()) {
			throw new RuntimeException("Withdraw not found");
		}
		Withdraw withdraw = withdrawOpt.get();
		withdraw.setDate(LocalDateTime.now());
		if (accept) {
			withdraw.setStatus(WithdrawalStatus.SUCCESS);
		} else {
			withdraw.setStatus(WithdrawalStatus.PENDING);
		}

		return withdrawRepository.save(withdraw);
	}

	@Override
	public List<Withdraw> getUsersWithdrawHistory(User user) {
		return withdrawRepository.findByUserId(user.getId());
	}

	@Override
	public List<Withdraw> getAllWithdrawRequest() {
		return withdrawRepository.findAll();
	}
}

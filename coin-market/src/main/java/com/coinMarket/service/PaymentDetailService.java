package com.coinMarket.service;

import com.coinMarket.model.PaymentDetail;
import com.coinMarket.model.User;
import com.coinMarket.repositories.IPaymentDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentDetailService implements IPaymentDetailService{

	IPaymentDetailRepository paymentDetailRepository;

	@Override
	public PaymentDetail addPaymentDetail(String accountNumber, String accountHolderName, String ifsc, String bankName, User user) {
		PaymentDetail paymentDetail = new PaymentDetail();
		paymentDetail.setAccountNumber(accountNumber);
		paymentDetail.setAccountHolderName(accountHolderName);
		paymentDetail.setIfsc(ifsc);
		paymentDetail.setBankName(bankName);
		paymentDetail.setUser(user);
		paymentDetail = paymentDetailRepository.save(paymentDetail);

		return paymentDetail;
	}

	@Override
	public PaymentDetail getUserPaymentDetail(User user) {
		return paymentDetailRepository.findByUserId(user.getId());
	}
}

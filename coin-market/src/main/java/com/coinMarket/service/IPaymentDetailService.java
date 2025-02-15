package com.coinMarket.service;

import com.coinMarket.model.PaymentDetail;
import com.coinMarket.model.User;

public interface IPaymentDetailService {
	PaymentDetail addPaymentDetail(String accountNumber, String accountHolderName, String ifsc, String bankName, User user);

	PaymentDetail getUserPaymentDetail (User user);
}

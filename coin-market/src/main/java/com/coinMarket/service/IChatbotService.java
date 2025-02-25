package com.coinMarket.service;

import com.coinMarket.response.ApiResponse;

public interface IChatbotService {
	ApiResponse getCoinDetails(String coinName) throws Exception;

	String simpleChat(String prompt);
}

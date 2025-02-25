package com.coinMarket.response;

import lombok.Data;

@Data
public class FunctionResponse {
	private String currencyName;
	private String functionName;
	private String currencyData;
}

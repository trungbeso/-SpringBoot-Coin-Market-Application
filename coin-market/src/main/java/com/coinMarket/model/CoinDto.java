package com.coinMarket.model;

import lombok.Data;

import java.util.Date;

@Data
public class CoinDto {
	private String id;
	private String name;
	private String symbol;
	private String image;
	private Double currentPrice;
	private Double marketCap;
	private int marketCapRank;
	private Double totalVolume;
	private Double high24h;
	private Double low24h;
	private Double priceChange24h;
	private Double priceChangePercentage24h;
	private Double marketCapChange24h;
	private Double marketCapChangePercentage24h;
	private Double circulatingSupply;
	private Double totalSupply;
	private Double ath;
	private Double athChangePercentage;
	private Date altDate;
	private Double altChangePercentage;
	private Date athDate;
}

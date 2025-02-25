package com.coinMarket.service;

import com.coinMarket.model.Coin;
import com.coinMarket.repositories.ICoinRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CoinService implements ICoinService {

	ICoinRepository coinRepository;
	ObjectMapper objectMapper;

	@Override
	public List<Coin> getAllCoins(int page) throws Exception {
		String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			List<Coin> coins = objectMapper.readValue(response.getBody(),
				  new TypeReference<List<Coin>>() {
				  });
			return coins;
		}catch (HttpClientErrorException | HttpServerErrorException e){
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String getMarketChart(String coinId, int days) throws Exception {
		String url = "https://api.coingecko.com/api/v3/coins/"+ coinId +
			  "/market_chart?vs_currency=usd&days=" + days;
		return getRestTemplate(url);
	}

	@Override
	public String getCoinDetail(String coinId) throws Exception {

		String url = "https://api.coingecko.com/api/v3/coins/"+ coinId;

		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			Coin coin = new Coin();
			coin.setId(jsonNode.get("id").asText());
			coin.setName(jsonNode.get("name").asText());
			coin.setSymbol(jsonNode.get("symbol").asText());
			coin.setImage(jsonNode.get("image").get("large").asText());

			JsonNode marketData = jsonNode.get("market_data");

			coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
			coin.setMarketCap(marketData.get("market_cap").get("usd").asDouble());
			coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
			coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
			coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
			coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
			coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
			coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
			coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
			coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
			//coin.setCirculatingSupply(marketData.get("circulating_supply").get("usd").asLong());
			coin.setTotalSupply(marketData.get("total_supply").asLong());
//			coin.setMaxSupply(marketData.get("max_supply").get("usd").asLong());
			coin.setAth(marketData.get("ath").get("usd").asDouble());

			coin = coinRepository.save(coin);

			return response.getBody();
		}catch (HttpClientErrorException | HttpServerErrorException e){
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Coin findById(String coinId) throws Exception {
		Optional<Coin> optionalCoin = coinRepository.findById(coinId);
		if (optionalCoin.isEmpty()) {
			throw new Exception("Coin not found");
		}
		return optionalCoin.get();
	}

	@Override
	public String searchCoin(String keyword) throws Exception {
		String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
		return getRestTemplate(url);
	}

	@Override
	public String getTop50CoinsByMarketCapRank() throws Exception {
		String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
		return getRestTemplate(url);
	}

	@Override
	public String getTrendingCoins() throws Exception {
		String url = "https://api.coingecko.com/api/v3/search/trending";
		return getRestTemplate(url);
	}

	private String getRestTemplate(String url) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			return response.getBody();
		}catch (HttpClientErrorException | HttpServerErrorException e){
			throw new Exception(e.getMessage());
		}
	}
}

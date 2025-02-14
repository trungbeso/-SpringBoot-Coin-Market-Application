package com.coinMarket.controller;

import com.coinMarket.model.Coin;
import com.coinMarket.service.ICoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/coins")
public class CoinController {

	ICoinService coinService;
	ObjectMapper objectMapper;

	@GetMapping
	ResponseEntity<List<Coin>> getCoins(@RequestParam("page") int page) throws Exception {
		List<Coin> coins = coinService.getAllCoins(page);
		return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{coinId}/chart")
	ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId,
	                                          @RequestParam("days") int days) throws Exception {
		String response = coinService.getMarketChart(coinId, days);
		JsonNode node = objectMapper.readTree(response);
		return new ResponseEntity<>(node, HttpStatus.ACCEPTED);
	}

	@GetMapping("/search")
	ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
		String coin = coinService.searchCoin(keyword);
		JsonNode node = objectMapper.readTree(coin);
		return ResponseEntity.ok(node);
	}

	@GetMapping("/top50")
	ResponseEntity<JsonNode> getTop50CoinByMarketCapBank() throws Exception {
		String coin = coinService.getTop50CoinsByMarketCapRank();
		JsonNode node = objectMapper.readTree(coin);
		return ResponseEntity.ok(node);
	}


	@GetMapping("/treading")
	ResponseEntity<JsonNode> getTreadingCoin() throws Exception {
		String coin = coinService.getTreadingCoins();
		JsonNode node = objectMapper.readTree(coin);
		return ResponseEntity.ok(node);
	}

	@GetMapping("/details/{coinId}")
	ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
		String coin = coinService.getCoinDetail(coinId);
		JsonNode node = objectMapper.readTree(coin);
		return ResponseEntity.ok(node);
	}
}

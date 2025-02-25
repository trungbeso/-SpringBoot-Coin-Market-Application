package com.coinMarket.service;

import com.coinMarket.model.CoinDto;
import com.coinMarket.response.ApiResponse;
import com.coinMarket.response.FunctionResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatbotService implements IChatbotService {

	//	@Value("")
	private final String GEMINI_API_KEY = "AIzaSyAX31hjh5Dz0cL9Poa_icVMF8CI_-jytLE";

	private double convertToDouble(Object value) {
		if (value instanceof Integer) {
			return Double.parseDouble(value.toString());
		} else if (value instanceof Long) {
			return Double.parseDouble(value.toString());
		} else if (value instanceof Double) {
			return (double) value;
		} else {
			throw new IllegalArgumentException("unsupported type: " + value.getClass().getName());
		}
	}

	public FunctionResponse getFunctionResponse(String prompt) {
		String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
			  + GEMINI_API_KEY;

		// Create Json request body using method chaining
		JSONObject requestBodyJson = new JSONObject()
			  .put("contents", new JSONArray()
					 .put(new JSONObject()
							.put("parts", new JSONArray()
								  .put(new JSONObject()
										 .put("text", prompt)
								  )
							)
					 )
			  ).put("tools", new JSONArray()
					 .put(new JSONObject()
							.put("functionDeclarations", new JSONArray()
								  .put(new JSONObject()
										 .put("name", "getCoinDetails")
										 .put("description", "Get the coin details from given currency object")
										 .put("parameters", new JSONObject()
												.put("type", "OBJECT")
												.put("properties", new JSONObject()
													  .put("currencyName", new JSONObject()
															 .put("type", "STRING").put("description",
																	"The currency name, id, symbol")).put("currencyData",
															 new JSONObject()
																	.put("type", "STRING")
																	.put("description",
																		  "Currency Data id, symbol, name, image, current_price, " +
																				 "market_cap, market_cap_rank, fully_diluted_valuation, " +
																				 "total_volume, high_24h, low_24h, price_change_24h, " +
																				 "price_change_percentage_24h, market_cap_change_24h, " +
																				 "market_cap_change_percentage_24h, circulating_supply, " +
																				 "total_supply, ath, ath_change_percentage, ath_date, " +
																				 "atl, atl_change_percentage, atl_date, last_updated.")
													  )
												).put("required", new JSONArray()
													  .put("currencyName")
													  .put("currencyData")
												)
										 )
								  )
							)
					 )
			  );

		// Create Http headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create the HTTP entity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);

		// Make the POST request method
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

		String responseBody = response.getBody();

		JSONObject jsonObject = new JSONObject(responseBody);

		//extract the first candidate
		JSONArray candidates = jsonObject.getJSONArray("candidates");
		JSONObject firstCandidate = candidates.getJSONObject(0);

		//extract the function call details
		JSONObject content = firstCandidate.getJSONObject("content");
		JSONArray parts = content.getJSONArray("parts");
		JSONObject firstPart = parts.getJSONObject(0);
		JSONObject functionCall = firstPart.getJSONObject("functionCall");

		String functionName = functionCall.getString("name");
		JSONObject args = functionCall.getJSONObject("args");
		String currencyName = args.getString("currencyName");
		String currencyData = args.getString("currencyData");

		//logging
		System.out.println("functionName: " + functionName);
		System.out.println("currencyName: " + currencyName);
		System.out.println("currencyData: " + currencyData);

		FunctionResponse res = new FunctionResponse();
		res.setFunctionName(functionName);
		res.setCurrencyData(currencyData);
		res.setCurrencyName(currencyName);

		return res;
	}

	public CoinDto makeApiRequest(String currencyName) throws Exception {
		String url = "https://api.coingecko.com/api/v3/coins/" + currencyName;

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
		Map<String, Object> responseBody = responseEntity.getBody();
		if (responseEntity != null) {
			Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
			Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");

			CoinDto coinDto = new CoinDto();
			coinDto.setId(responseBody.get("id").toString());
			coinDto.setName(responseBody.get("name").toString());
			coinDto.setSymbol(responseBody.get("symbol").toString());
			coinDto.setImage(image.get("large").toString());

			//market data
			coinDto.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd")));
			coinDto.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
			coinDto.setMarketCapRank((Integer) marketData.get("market_cap_rank"));
			coinDto.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
			coinDto.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
			coinDto.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
			coinDto.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")));
			coinDto.setPriceChangePercentage24h(convertToDouble(marketData.get(
				  "price_change_percentage_24h")));
			coinDto.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
			coinDto.setMarketCapChangePercentage24h(convertToDouble(marketData.get(
				  "market_cap_change_percentage_24h")));
			coinDto.setTotalSupply(convertToDouble(marketData.get("total_supply")));
			return coinDto;
		}
		throw new Exception("coin not found . . .");
	}

	@Override
	public ApiResponse getCoinDetails(String prompt) throws Exception {

		FunctionResponse res = getFunctionResponse(prompt);
		CoinDto apiResponse = makeApiRequest(res.getCurrencyName().toLowerCase());

		String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
			  + GEMINI_API_KEY;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create JSON body using method chaining
		String body = new JSONObject()
			  /* State 1: Provide the prompt */
			  .put("contents", new JSONArray()
					 .put(new JSONObject()
							.put("role", "user")
							.put("parts", new JSONArray()
								  .put(new JSONObject()
										 .put("text", prompt)
								  )
							)
					 )
					 /* State 2: Describe the function */
					 .put(new JSONObject()
							.put("role", "model")
							.put("parts", new JSONArray()
								  .put(new JSONObject()
										 .put("functionCall", new JSONObject()
												.put("name", "getCoinDetails")
												.put("args", new JSONObject()
													  .put("currencyName", res.getCurrencyName())
													  .put("currencyData", res.getCurrencyData())
												)
										 )
								  )
							)
					 )
					 /* State 3: Get the function necessary params */
					 .put(new JSONObject()
							.put("role", "function")
							.put("parts", new JSONArray()
								  .put(new JSONObject()
										 .put("functionResponse", new JSONObject() //function from CoinGeko API
												.put("name", "getCoinDetails")
												.put("response", new JSONObject()
													  .put("name", "getCoinDetails")
													  .put("content", apiResponse)
												)
										 )
								  )
							)
					 )
			  ).put("tools", new JSONArray()
					 .put(new JSONObject()
							.put("functionDeclarations", new JSONArray()
								  .put(new JSONObject()
										 .put("name", "getCoinDetails")
										 .put("description", "Get crypto currency data from given currency object.")
										 .put("parameters", new JSONObject()
											   .put("type", "OBJECT")
											   .put("properties", new JSONObject()
												     .put("currencyName", new JSONObject()
															 .put("type", "STRING")
															 .put("description", "The currency Name, id, symbol.")
												     )
													  .put("currencyData", new JSONObject()
															 .put("type", "STRING")
															 .put("description", "The currency data id, symbol, current price, " +
																	"image, market cap rank, market cap extra")))
												.put("required", new JSONArray()
													  .put("currencyName")
													  .put("currencyData")
												)
										 )
								  )
							)
					 )
			  ).toString();


		HttpEntity<String> request = new HttpEntity<>(body, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);

		String responseBody = response.getBody();

		System.out.println("----final response ----" + responseBody);

		JSONObject jsonObject = new JSONObject(responseBody);

		//extract the first candidate
		JSONArray candidates = jsonObject.getJSONArray("candidates");
		JSONObject firstCandidate = candidates.getJSONObject(0);

		// Extract the text
		JSONObject content = firstCandidate.getJSONObject("content");
		JSONArray parts = content.getJSONArray("parts");
		JSONObject firstPart = parts.getJSONObject(0);
		String text = firstPart.getString("text");

		ApiResponse answer = new ApiResponse();
		answer.setMessage(text);

		return answer;
	}

	@Override
	public String simpleChat(String prompt) {
		String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
			  + GEMINI_API_KEY;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = new JSONObject()
			  .put("contents", new JSONArray()
					 .put(new JSONObject()
							.put("parts", new JSONArray()
								  .put(new JSONObject()
										 .put("text", prompt)))))
			  .toString();

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

		try {
			JSONObject responseJson = new JSONObject(response.getBody());
			JSONArray candidates = responseJson.getJSONArray("candidates");
			if (candidates.length() > 0) {
				JSONObject candidate = candidates.getJSONObject(0);
				JSONObject content = candidate.getJSONObject("content");
				JSONArray parts = content.getJSONArray("parts");
				if (parts.length() > 0) {
					JSONObject part = parts.getJSONObject(0);
					return part.getString("text");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace(); // Handle JSON parsing error appropriately
			return "Error parsing Gemini response: " + e.getMessage();
		}

		return "No text response found in Gemini response."; // Handle cases where text is not found
	}

}

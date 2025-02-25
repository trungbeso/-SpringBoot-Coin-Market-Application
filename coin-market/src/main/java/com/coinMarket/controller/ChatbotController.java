package com.coinMarket.controller;

import com.coinMarket.request.PromptRequest;
import com.coinMarket.response.ApiResponse;
import com.coinMarket.service.IChatbotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat")
public class ChatbotController {

	private final IChatbotService chatbotService;

	public ChatbotController(IChatbotService chatbotService) {
		this.chatbotService = chatbotService;
	}


	@PostMapping
	public ResponseEntity<ApiResponse> getCoinDetails(@RequestBody PromptRequest prompt) throws Exception {

		ApiResponse response = chatbotService.getCoinDetails(prompt.getPrompt());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/simple")
	public ResponseEntity<String> simpleChatHandler(@RequestBody PromptRequest prompt) throws Exception {
		String response = chatbotService.simpleChat(prompt.getPrompt());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

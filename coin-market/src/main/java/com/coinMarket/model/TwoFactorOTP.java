package com.coinMarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorOTP {

	@Id
	private String id;

	private String otp;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	@OneToOne
	private User user;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String jwt;
}

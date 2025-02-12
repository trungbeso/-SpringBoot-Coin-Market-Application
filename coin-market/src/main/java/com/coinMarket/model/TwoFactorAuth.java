package com.coinMarket.model;

import com.coinMarket.enums.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
	private boolean isEnabled = false;
	private VerificationType sendTo;
}

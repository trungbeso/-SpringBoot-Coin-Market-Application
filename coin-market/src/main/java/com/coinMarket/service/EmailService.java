package com.coinMarket.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	JavaMailSender mailSender;

	public void sendEmailVerificationEmail(String email, String otp) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
		String subject = "Verify OTP";
		String text = "Your verification code is " + otp;

		messageHelper.setSubject(subject);
		messageHelper.setText(text);
		messageHelper.setTo(email);

		try {
			mailSender.send(mimeMessage);
		} catch (MailException e) {
			throw new MailSendException(e.getMessage());
		}
	}
}

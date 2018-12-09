package com.garchkorelation.service;

public interface EmailSenderService {

	void sendEmail(String toAddress,String subject, String message);
	
	void sendVerification();

}
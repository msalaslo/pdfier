package com.msl.pdfier.commons.security;

import org.springframework.stereotype.Component;

@Component
public class AccessControlService {
	public AccessControlService() {
	}

	public boolean validReferrer(String referrer) {
		if ((referrer != null) && 
				((referrer.startsWith("http://pdfier.com"))
				|| (referrer.startsWith("http://www.pdfier.com")) 
				|| (referrer.startsWith("https://pdfier.com"))
				|| (referrer.startsWith("https://www.pdfier.com"))
				|| (referrer.startsWith("http://localhost")))) {
			return true;
		}
		return false;
	}

	public boolean validLicense(String license) {
		if ((license != null) && (license.equals("Y2FuZGVsYWNpcmNlcm9zZW5kbw=="))) {
			return true;
		}
		return false;
	}
}

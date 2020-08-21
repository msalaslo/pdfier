package com.msl.pdfier.openpdf.app.security;

import org.springframework.stereotype.Component;

@Component
public class AccessControlService {
	public AccessControlService() {
	}

	public boolean validReferrer(String referrer) {
		if ((referrer != null) && ((referrer.startsWith("http://pdfier.com"))
				|| (referrer.startsWith("http://www.pdfier.com")) || (referrer.startsWith("http://localhost/")))) {
			return true;
		}
		return false;
	}

	public boolean validLisence(String lisence) {
		if ((lisence != null) && (lisence.equals("Y2FuZGVsYWNpcmNlcm9zZW5kbw=="))) {
			return true;
		}
		return false;
	}
}

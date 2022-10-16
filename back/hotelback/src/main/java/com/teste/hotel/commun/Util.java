package com.teste.hotel.commun;

public class Util {

	public String getCauseMessage(Throwable t) {		
	
		while (t.getCause() != null) {			
			t = t.getCause();
		}
		return t.getLocalizedMessage();
	}

}

package Utils;

public class TZPBeforeTest {
	
	public static String Umgebung() {
	  // Festlegung, in welcher Umgebung getestet wird 	

	// Stage 
	return "http://52.59.225.49";
		
//	// Sales	
//	return "http://18.157.183.108";
	  
//	// Produktion	
//	return "https://portal.forsa-topzins.de";	  
	
//	// New Flow - Mehrere User
//	return "http://3.127.85.31";
			
	}

	public static String AdminEmail() {
		 // Festlegung des Adminzugangs  
		
		    // Stage Version  
		    return "admintest@forsa-gmbh.de";
			
//			// Sales	
//			return "admintest@forsa-gmbh.de";
			  
//			// Produktion	
//			return "topzins@forsa-gmbh.de";	  
			
//			// New Flow - Mehrere User
//		    return "topzins@forsa-gmbh.de";
		}
	
	public static String AdminPasswort() {
		 // Festlegung des Adminpassworts  

	    // Stage Version  
	    return "Test12345!";
		
//		// Sales	
//		return "Test12345!";
		  
//		// Produktion	
//		return "phaiweighui%Joteghe6";	  
		
//		// New Flow - Mehrere User
//		return "Test12345!";
		}	
	
	public static String BrowserArt() {
	 // Festlegung des Browsers 
		return "Chrome";
		
//	 // Festlegung des Browsers 
//		return "Firefox";
	}
	
	public static Integer Pausenzeit() {
		 // Festlegung der Zeitspanne  
			return 100;
		}
	
	
}

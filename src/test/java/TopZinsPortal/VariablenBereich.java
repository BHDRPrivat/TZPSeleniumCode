package TopZinsPortal;


public interface VariablenBereich {
	
	// Einige zentrale Variablen werden im Interface festgelegt und in die Klassen übergeben
	public Integer Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();
	public String StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
	public String SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
	
}

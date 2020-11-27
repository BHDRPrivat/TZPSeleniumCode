//Admin login

//Button "Daten komplett" in menu clicken
Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'DATEN KOMPLETT')]");

//Firmenname in das Suchfeld eingeben
Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Suchbegriff);

//???????
//In der ersten Zeile wird dann der gesuchter user
//Icon "Bearbeiten" clicken
Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//label[contains(@class, 'MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary')]");

//Section "Dokumente" auswaehlen
Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'Dokumente')]");

//Ein Häckchen in der ersten check-box setzen
Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//label[contains(@class, 'MuiButtonBase-root MuiIconButton-root jss2322 MuiCheckbox-root jss2320')]");

//Button "Freigeben" clicken
Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//label[contains(@class, 'MuiTouchRipple-root')]");
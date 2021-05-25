package unofficial.bluematchgroup.bluematch.config;

public enum Resource {

    FXML_MAIN ("/fxml/BlueMatch.fxml"),

    FXML_ADD_AANBIEDING ("/fxml/AddAanbieding.fxml"),
    FXML_ADD_AANVRAAG ("/fxml/AddAanvraag.fxml"),
    FXML_ADD_BROKER ("/fxml/AddBroker.fxml"),
    FXML_ADD_KLANT ("/fxml/AddKlant.fxml"),
    FXML_ADD_MEDEWERKER ("/fxml/AddMedewerker.fxml"),

    FXML_EDIT_AANBOD ("/fxml/EditAanbod.fxml"),
    FXML_EDIT_AANVRAAG ("/fxml/EditAanvraag.fxml"),
    
    FXML_OVERZICHT_BROKER ("/fxml/OverzichtBroker.fxml"),
    FXML_OVERZICHT_DETAILS ("/fxml/OverzichtDetails.fxml"),
    FXML_OVERZICHT_KLANT ("/fxml/OverzichtKlant.fxml"),
    FXML_OVERZICHT_MEDEWERKER ("/fxml/OverzichtMedewerker.fxml");

    private final String path;

    private Resource(String s) {
        path = s;
    }

    @Override
    public String toString() {
        return this.path;
    }
}

package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Medewerker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddMedewerkerController extends BaseController {

    @FXML
    private ChoiceBox<String> statusmdwBox;
    @FXML
    private TextField voornaamField;
    @FXML
    private TextField achternaamField;
    @FXML
    private TextField urenperweekField;
    @FXML
    private TextField emailmedewerkerField;
    @FXML
    private TextArea opmerkingmedewerkerField;
    @FXML
    private TableView<Medewerker> medewerkerTable;
    @FXML
    private Label titelmdwlabel;

    private ObservableList<String> options = FXCollections.observableArrayList("Volledig ingezet bij klant",
    "Beschikbaar", "Gedeeltelijk beschikbaar", "Niet beschikbaar (Ziekte, verlof)");

    @FXML
    private void initialize() {
        statusmdwBox.setItems(options);
        statusmdwBox.setValue("Beschikbaar");
    }

    protected Medewerker getNewMedewerker() {
        titelmdwlabel.setText("Medewerker toevoegen");
        String voornaammdw = voornaamField.getText();
        String achternaammdw = achternaamField.getText();
        String urenperweekmdw = urenperweekField.getText();
        String emailmdw = emailmedewerkerField.getText();
        String opmerkingmdw = opmerkingmedewerkerField.getText();
        // String statusmdw = statusmdwBox.getValue();

        Medewerker newMedewerker = new Medewerker();
        newMedewerker.setVoornaam(voornaammdw);
        newMedewerker.setAchternaam(achternaammdw);
        newMedewerker.setUren(urenperweekmdw);
        newMedewerker.setStatusmdw(statusmdwBox.getValue());
        newMedewerker.setEmailmedewerker(emailmdw);
        newMedewerker.setOpmerkingmedewerker(opmerkingmdw);

        return newMedewerker;
    }

    protected void editMedewerker(Medewerker medewerker, String type) {
        if (type.equals("update")) {
            titelmdwlabel.setText("Medewerker wijzigen");
        } else {
            titelmdwlabel.setText("Medewerker verwijderen ?");
        }
        voornaamField.setText(medewerker.getVoornaam());
        achternaamField.setText(medewerker.getAchternaam());
        urenperweekField.setText(medewerker.getUrenperweek());
        emailmedewerkerField.setText(medewerker.getEmailmedewerker());
        opmerkingmedewerkerField.setText(medewerker.getOpmerkingmedewerker());
        statusmdwBox.setValue(medewerker.getStatusmdw());
    }

    protected void updateMedewerker(Medewerker medewerker) {
        medewerker.setVoornaam(voornaamField.getText());
        medewerker.setAchternaam(achternaamField.getText());
        medewerker.setUren(urenperweekField.getText());
        medewerker.setEmailmedewerker(emailmedewerkerField.getText());
        medewerker.setOpmerkingmedewerker(opmerkingmedewerkerField.getText());
        medewerker.setStatusmdw(statusmdwBox.getValue());
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_ADD_MEDEWERKER;
    }
}

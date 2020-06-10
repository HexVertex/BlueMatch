package BlueMatch;

import BlueMatch.model.Klant;
import BlueMatch.model.Medewerker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddKlantController {


    @FXML
    private TextField klantnaamField;
    @FXML
    private TextField klantcontactpersoonField;
    @FXML
    private TextField klantcontacttelnrField;
    @FXML
    private TextField klantcontactemailField;
    @FXML
    private TextArea klantopmerkingField;

    @FXML
    private TableView<Klant> klantTable;
 @FXML
 private Label titellabel;

    public Klant getNewKlant() {
        titellabel.setText("Klant toevoegen");
        String klantnaam = klantnaamField.getText();
        String klantcontactpersoon = klantcontactpersoonField.getText();
        String klantcontacttelnr = klantcontacttelnrField.getText();
        String klantcontactemail = klantcontactemailField.getText();
        String klantopmerking = klantopmerkingField.getText();


        Klant newKlant = new Klant();
        newKlant.setKlantnaam(klantnaam);
        newKlant.setKlantcontactpersoon(klantcontactpersoon);
        newKlant.setKlantcontacttelnr(klantcontacttelnr);
        newKlant.setKlantcontactemail(klantcontactemail);
        newKlant.setKlantopmerking(klantopmerking);

        return newKlant;
    }




    public void editKlant(Klant klant) {
     titellabel.setText("Klantgegevens bewerken");
        klantnaamField.setText(klant.getKlantnaam());
        klantcontactpersoonField.setText(klant.getKlantcontactpersoon());
        klantcontacttelnrField.setText(klant.getKlantcontacttelnr());
        klantcontactemailField.setText(klant.getKlantcontactemail());
        klantopmerkingField.setText(klant.getKlantopmerking());
    }

    public void updateKlant (Klant klant){
        klant.setKlantnaam(klantnaamField.getText());
        klant.setKlantcontactpersoon(klantcontactpersoonField.getText());
        klant.setKlantcontacttelnr(klantcontacttelnrField.getText());
        klant.setKlantcontactemail(klantcontactemailField.getText());
        klant.setKlantopmerking(klantopmerkingField.getText());
    }
}
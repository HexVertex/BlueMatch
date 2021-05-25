package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.model.Medewerker;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Aanbod;
import unofficial.bluematchgroup.bluematch.model.DataSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class AddAanbiedingController implements IController{
    @FXML
    private TextField medewerkerField;
    @FXML
    private TextField tariefaanbodField;
    @FXML
    private TextField urenperweekaanbodField;
    @FXML
    private TextField statusaanbodField;
    @FXML
    private TextArea opmerkingaanbodField;

    @FXML
    private TableView<Aanbod> aanbodTable;
    @FXML
    private Label Dialogue;

    @FXML
    private ChoiceBox<String> selectMedewerkerBox;
    @FXML
    private ChoiceBox<String> aanbodBox;

    private ObservableList<String> populateMdwNameList() {
        ArrayList<String> medewerkernaamlist = new ArrayList<>();
        ObservableList<Medewerker> medewerkerslijst = FXCollections
                .observableArrayList(DataSource.getInstance().queryMedewerker());

        for (Medewerker huidigemdw : medewerkerslijst) {
            medewerkernaamlist.add(huidigemdw.getVoornaam() + " " + huidigemdw.getAchternaam());
        }

        return FXCollections.observableArrayList(medewerkernaamlist);
    }

    @FXML
    private void initialize() {
        ObservableList<String> optionsstatus = FXCollections.observableArrayList("Nieuw", "Uitgenodigd voor gesprek",
                "Plaatsing", "Afgewezen", "Teruggetrokken", "Overig");

        ObservableList<String> options = populateMdwNameList();
        selectMedewerkerBox.setItems(options);
        aanbodBox.setItems(optionsstatus);
    }

    protected Aanbod getNewAanbod(String idaanvraag) {
        String medewerker = selectMedewerkerBox.getValue();
        String tariefaanbod = tariefaanbodField.getText();
        String urenperweekaanbod = urenperweekaanbodField.getText();
        String statusaanbod = aanbodBox.getValue();
        String opmerkingaanbod = opmerkingaanbodField.getText();

        Aanbod newAanbod = new Aanbod();
        // System.out.println(medewerker);
        newAanbod.setRefmedewerker(medewerker);
        newAanbod.setStatusaanbod(statusaanbod);
        newAanbod.setTariefaanbod(tariefaanbod);
        newAanbod.setUrenperweekaanbod(urenperweekaanbod);
        newAanbod.setOpmerkingaanbod(opmerkingaanbod);
        newAanbod.setRefaanvraag(idaanvraag);

        return newAanbod;
    }

    protected void editAanbod(Aanbod aanbod, String type) {
        if (type.equals("update")) {
            Dialogue.setText("Aanbieding wijzigen");
            // System.out.println("update selected");
        } else {
            Dialogue.setText("Aanbieding verwijderen ?");
            // System.out.println("delete selected");
        }
        // headertext.setText("Aanbod wijzigen");
        selectMedewerkerBox.setValue(aanbod.getRefmedewerker());
        tariefaanbodField.setText(aanbod.getTariefaanbod());
        urenperweekaanbodField.setText(aanbod.getUrenperweekaanbod());
        aanbodBox.setValue(aanbod.getStatusaanbod());
        opmerkingaanbodField.setText(aanbod.getOpmerkingaanbod());

    }

    protected void updateAanbod(Aanbod aanbod) {
        aanbod.setRefmedewerker(selectMedewerkerBox.getValue());
        aanbod.setTariefaanbod(tariefaanbodField.getText());
        aanbod.setUrenperweekaanbod(urenperweekaanbodField.getText());
        aanbod.setStatusaanbod(aanbodBox.getValue());
        aanbod.setOpmerkingaanbod(opmerkingaanbodField.getText());

    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_ADD_AANBIEDING;
    }

    @Override
    public void updateView() {}

    @Override
    public void refreshScreen() {}
}

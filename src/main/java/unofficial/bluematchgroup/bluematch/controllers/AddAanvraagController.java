package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Aanvraag;
import unofficial.bluematchgroup.bluematch.model.Broker;
import unofficial.bluematchgroup.bluematch.model.DataSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.time.LocalDate.parse;

public class AddAanvraagController implements IController {

    @FXML
    private ChoiceBox<String> statusklantBox;
    @FXML
    private ChoiceBox<String> selectBrokerBox;
    @FXML
    private DatePicker datePickAanvraagDate;
    @FXML
    private DatePicker datePickerStartDate;

    private ObservableList<String> options = FXCollections.observableArrayList("Nieuw", "Vrijblijvend aangeboden",
            "Aangeboden Broker", "Aangeboden Eindklant", "Aangeboden");

    @FXML
    private void initialize() {
        statusklantBox.setItems(options);
        statusklantBox.setValue("Nieuw");
        ObservableList<String> optionsbrkr = populateBrokerNameList();
        selectBrokerBox.setItems(optionsbrkr);
    }

    @FXML
    private TextField BrokerField;
    @FXML
    private TextField ContactField;
    @FXML
    private TextField FunctieField;
    @FXML
    private TextField UrenPerWeekField;
    // @FXML
    // private TextField StatusKlantField;
    @FXML
    private TextField DatumAanvraagField;
    @FXML
    private TextField LocatieField;
    @FXML
    private TextField StartDatumField;
    @FXML
    private TextArea OpmerkingField;
    @FXML
    private TextField KlantField;
    @FXML
    private TextField LinkField;
    @FXML
    private TextField TariefField;
    @FXML
    private Label Dialogue;
    @FXML
    private TableView<Aanvraag> aanvraagTable;

    private ObservableList<String> populateBrokerNameList() {
        ArrayList<String> brokernaamlist = new ArrayList<String>();
        ObservableList<Broker> medewerkerslijst = FXCollections
                .observableArrayList(DataSource.getInstance().queryBroker());

        for (Broker huidigebroker : medewerkerslijst) {
            brokernaamlist.add(huidigebroker.getBrokernaam());
        }
        ObservableList<String> options = FXCollections.observableArrayList(brokernaamlist);

        return options;
    }

    protected Aanvraag getNewAanvraag() {
        // SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        String datumaanvraag = "";
        String startdatum = "";
        String broker = BrokerField.getText();
        String contact = ContactField.getText();
        String functie = FunctieField.getText();
        String urenperweek = UrenPerWeekField.getText();
        String statusklant = statusklantBox.getValue();
        if (datePickAanvraagDate.getValue() != null) {
            datumaanvraag = datePickAanvraagDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        String locatie = LocatieField.getText();
        if (datePickerStartDate.getValue() != null) {
            startdatum = datePickerStartDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        String opmerking = OpmerkingField.getText();

        String klant = KlantField.getText();

        String link = LinkField.getText();
        String tarief = TariefField.getText();

        Aanvraag newAanvraag = new Aanvraag();

        newAanvraag.setRefbroker(broker);
        newAanvraag.setRefcontact(contact);
        newAanvraag.setFunctie(functie);
        newAanvraag.setVraagurenweek(urenperweek);
        newAanvraag.setStatusklant(statusklant);
        newAanvraag.setDatumaanvraag(datumaanvraag);
        newAanvraag.setLocatie(locatie);
        newAanvraag.setStartdatum(startdatum);
        newAanvraag.setOpmerking(opmerking);
        newAanvraag.setRefklant(klant);
        newAanvraag.setLinkaanvraag(link);
        newAanvraag.setTariefaanvraag(tarief);
        return newAanvraag;
    }

    protected void editAanvraag(Aanvraag aanvraag, String type) {
        if (type == "update") {
            Dialogue.setText("Aanvraag wijzigen");
            // System.out.println("update selected");
        } else {
            Dialogue.setText("Aanvraag verwijderen ?");
            // System.out.println("delete selected");
        }

        BrokerField.setText(aanvraag.getRefbroker());
        ContactField.setText(aanvraag.getRefcontact());

        FunctieField.setText(aanvraag.getFunctie());
        selectBrokerBox.setValue(aanvraag.getRefbroker());
        UrenPerWeekField.setText(aanvraag.getVraagurenweek());
        statusklantBox.setValue(aanvraag.getStatusklant());
        // StartDatumField.setText(aanvraag.getStartdatum());
        LocatieField.setText(aanvraag.getLocatie());
        // DatumAanvraagField.setText(aanvraag.getDatumaanvraag());
        if ((aanvraag.getStartdatum()).isEmpty() == true) {
        } else {

            datePickerStartDate.setValue(LOCAL_DATE(aanvraag.getStartdatum()));
        }

        if (aanvraag.getDatumaanvraag().isEmpty() == true) {
        } else {

            datePickAanvraagDate.setValue(LOCAL_DATE(aanvraag.getDatumaanvraag()));
        }

        OpmerkingField.setText(aanvraag.getOpmerking());

        KlantField.setText(aanvraag.getRefklant());
        LinkField.setText(aanvraag.getLinkaanvraag());
        TariefField.setText(aanvraag.getTariefaanvraag());
    }

    protected void updateAanvraag(Aanvraag aanvraag) {

        aanvraag.setRefbroker(selectBrokerBox.getValue());
        aanvraag.setRefcontact(ContactField.getText());
        aanvraag.setFunctie(FunctieField.getText());
        aanvraag.setVraagurenweek(UrenPerWeekField.getText());
        aanvraag.setStatusklant(statusklantBox.getValue());
        if (datePickAanvraagDate.getValue() != null) {
            aanvraag.setDatumaanvraag(
                    datePickAanvraagDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        aanvraag.setLocatie(LocatieField.getText());
        if (datePickerStartDate.getValue() != null) {
            aanvraag.setStartdatum(datePickerStartDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        aanvraag.setOpmerking(OpmerkingField.getText());

        aanvraag.setRefklant(KlantField.getText());
        aanvraag.setLinkaanvraag(LinkField.getText());
        aanvraag.setTariefaanvraag(TariefField.getText());
    }

    private static LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate localDate = parse(dateString, formatter);
        return localDate;
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_ADD_AANVRAAG;
    }

    @Override
    public void updateView() {}

    @Override
    public void refreshScreen() {}
}

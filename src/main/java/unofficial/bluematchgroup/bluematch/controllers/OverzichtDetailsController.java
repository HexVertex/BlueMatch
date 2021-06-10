package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Aanbod;
import unofficial.bluematchgroup.bluematch.model.Aanvraag;
import unofficial.bluematchgroup.bluematch.model.DataSource;
import unofficial.bluematchgroup.bluematch.model.OverviewRecord;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class OverzichtDetailsController extends BaseController {

    @FXML
    private TextField refbrokerfield;
    @FXML
    private TextField refmedewerkerfield;
    @FXML
    private TextField refcontactfield;
    @FXML
    private TextField functiefield;
    @FXML
    private TextField locatiefield;
    @FXML
    private TextField refurenperweekaanbodfield;
    @FXML
    private TextField vraagurenfield;
    @FXML
    private TextField startdatumfield;
    @FXML
    private TextField aanvraagdatumfield;
    @FXML
    private TextField tariefaanvraagfield;
    @FXML
    private TextField tariefaanbodfield;
    @FXML
    private TextArea opmerkingfield;
    @FXML
    private TextArea opmerkingaanbodfield;
    @FXML
    private TextField linkaanvraagfield;
    @FXML
    private TextField statusklantfield;
    @FXML
    private TextField statusaanbodfield;
    @FXML
    private TextField klantfield;
    @FXML
    private int idaanbodfield;
    @FXML
    private int idaanvraagfield;

    private Aanvraag aanvraag = new Aanvraag();
    private Aanbod aanbod = new Aanbod();
    private OverviewRecord overviewrecordtmp = new OverviewRecord();

    // @FXML
    // private ComboBox<String> statusKlantCombo;
    //
    // ObservableList<String> options =
    // FXCollections.observableArrayList(
    // "", "Nieuw",
    // "Vrijblijvend aangeboden",
    // "Aangeboden Broker",
    // "Aangeboden Eindklant",
    // "Aangeboden"
    //
    //
    // );
    // @FXML
    // private ComboBox<String> statusAanbiedingCombo;
    //
    // ObservableList<String> optionsaanb =
    // FXCollections.observableArrayList(
    // "",
    // "Nieuw",
    // "Uitgenodigd voor gesprek",
    // "Plaatsing",
    // "Afgewezen",
    // "Teruggetrokken",
    // "Overig"
    // );
    //
    // @FXML
    // private void initialize() {
    // statusKlantCombo.setItems(options);
    // statusKlantCombo.setValue("");
    // statusAanbiedingCombo.setItems(optionsaanb);
    // statusAanbiedingCombo.setValue("");
    // }

    void listDetailsRecord(OverviewRecord overviewrecord) {

        overviewrecordtmp = overviewrecord;
        refbrokerfield.setText(overviewrecord.getRefbroker());
        refmedewerkerfield.setText(overviewrecord.getMedewerker());
        refcontactfield.setText(overviewrecord.getRefcontact());
        functiefield.setText(overviewrecord.getFunctie());
        locatiefield.setText(overviewrecord.getLocatie());
        vraagurenfield.setText(overviewrecord.getVraagurenweek());
        refurenperweekaanbodfield.setText(overviewrecord.getUrenperweekaanbod());
        startdatumfield.setText(overviewrecord.getStartdatum());
        aanvraagdatumfield.setText(overviewrecord.getDatumaanvraag());
        tariefaanvraagfield.setText(overviewrecord.getTariefaanvraag());
        tariefaanbodfield.setText(overviewrecord.getTariefaanbod());
        opmerkingfield.setText(overviewrecord.getOpmerking());
        // System.out.println(overviewrecord.getOpmerking());
        opmerkingaanbodfield.setText(overviewrecord.getOpmerkingaanbod());
        linkaanvraagfield.setText(overviewrecord.getLinkaanvraag());
        statusklantfield.setText(overviewrecord.getStatusklant());
        statusaanbodfield.setText(overviewrecord.getStatusaanbod());
        idaanbodfield = overviewrecord.getIdaanbod();
        idaanvraagfield = overviewrecord.getIdaanvraag();
        klantfield.setText(overviewrecord.getRefklant());

        aanvraag.setIdaanvraag(overviewrecord.getIdaanvraag());
        aanvraag.setRefbroker(overviewrecord.getRefbroker());
        aanvraag.setRefcontact(overviewrecord.getRefcontact());
        aanvraag.setFunctie(overviewrecord.getFunctie());
        aanvraag.setVraagurenweek(overviewrecord.getVraagurenweek());
        aanvraag.setStatusklant(overviewrecord.getStatusklant());
        if (overviewrecord.getDatumaanvraag() != null) {
            aanvraag.setDatumaanvraag(overviewrecord.getDatumaanvraag());
        } else {
            aanvraag.setDatumaanvraag(null);
        }

        aanvraag.setLocatie(overviewrecord.getLocatie());
        if (overviewrecord.getStartdatum() != null) {
            aanvraag.setStartdatum(overviewrecord.getStartdatum());
        } else {
            aanvraag.setStartdatum(null);
        }

        aanvraag.setOpmerking(overviewrecord.getOpmerking());
        aanvraag.setRefklant(overviewrecord.getRefklant());
        aanvraag.setLinkaanvraag(overviewrecord.getLinkaanvraag());
        aanvraag.setTariefaanvraag(overviewrecord.getTariefaanvraag());

        aanbod.setIdaanbod(overviewrecord.getIdaanbod());
        aanbod.setRefmedewerker(overviewrecord.getMedewerker());
        aanbod.setUrenperweekaanbod(overviewrecord.getUrenperweekaanbod());
        aanbod.setTariefaanbod(overviewrecord.getTariefaanbod());
        aanbod.setOpmerkingaanbod(overviewrecord.getOpmerkingaanbod());
        aanbod.setStatusaanbod(overviewrecord.getStatusaanbod());

    }

    @FXML
    public void modaanvraag(MouseEvent event) throws IOException {

        if (aanvraag != null) {
            Pair<AddAanvraagController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_AANVRAAG, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddAanvraagController addaanvraagcontroller = dialogSet.getKey();
            addaanvraagcontroller.editAanvraag(aanvraag, "update");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                addaanvraagcontroller.updateAanvraag(aanvraag);
                DataSource.getInstance().updateAanvraag(aanvraag);
            }
        }
        // System.out.println("Object: " + this);
        listDetailsRecord(DataSource.getInstance().getOverviewDetails(overviewrecordtmp.getIdaanbod()));

    }

    @FXML
    public void modaanbod(MouseEvent event) throws IOException {
        if (aanbod != null) {
            Pair<AddAanbiedingController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_AANBIEDING, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddAanbiedingController addaanbodcontroller = dialogSet.getKey();
            addaanbodcontroller.editAanbod(aanbod, "update");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                addaanbodcontroller.updateAanbod(aanbod);
                DataSource.getInstance().updateAanbod(aanbod);
            }
        }

        listDetailsRecord(DataSource.getInstance().getOverviewDetails(overviewrecordtmp.getIdaanbod()));

    }

    @FXML
    public void changeSceneMain(ActionEvent event) {
        WindowManager.getInstance().returnToMain();
    }

    @FXML
    public void changeSceneAanvraagDetails(ActionEvent event) throws IOException {
        WindowManager.getInstance().changeScene(Resource.FXML_EDIT_AANVRAAG);
    }

    @FXML
    public void overzichtMedewerker(ActionEvent event) throws IOException {
        WindowManager.getInstance().changeScene(Resource.FXML_OVERZICHT_MEDEWERKER);
    }

    @FXML
    public void overzichtBroker(ActionEvent event) throws IOException {

        WindowManager.getInstance().changeScene(Resource.FXML_OVERZICHT_BROKER);
    }

    @FXML
    public void overzichtKlant(ActionEvent event) throws IOException {
        WindowManager.getInstance().changeScene(Resource.FXML_OVERZICHT_KLANT);
    }

    @FXML
    public void changeSceneAanbodDetails(ActionEvent event) throws IOException {
        WindowManager.getInstance().changeScene(Resource.FXML_EDIT_AANBOD);
        // FXMLLoader loader = new FXMLLoader(getClass().getResource(Resource.FXML_EDIT_AANBOD.toString()));
        // Parent detailViewParent = loader.load();
        // EditAanbodController ctrleditaanbod = loader.getController();

        // ctrleditaanbod.listAanbiedingen();
        // Scene detailViewScene = new Scene(detailViewParent);
        // Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // window.setScene((detailViewScene));
        // window.show();
        // window.widthProperty().addListener((obs, oldVal, newVal) -> {
        //     Config.getInstance().windowWidth = (int) window.getWidth();

        //     ctrleditaanbod.updateView();
        // });
    }

    public void updateMainView() {
        System.out.println("update view in detailscreen");
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_OVERZICHT_DETAILS;
    }

    @Override
    public void updateView() {
    }

    @Override
    public void refreshScreen() {
    }
}
package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Config;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.DataSource;
import unofficial.bluematchgroup.bluematch.model.Klant;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class OverzichtKlantController extends BaseController {

    @FXML
    private TableColumn<TableView<Klant>, String> columnklantnaam;
    @FXML
    private TableColumn<TableView<Klant>, String> columnklantcontactpersoon;
    @FXML
    private TableColumn<TableView<Klant>, String> columnklantcontacttelnr;
    @FXML
    private TableColumn<TableView<Klant>, String> columnklantcontactemail;
    @FXML
    private TableColumn<TableView<Klant>, String> columnklantopmerking;
    @FXML
    private Button btnklanttoevoegen;
    @FXML
    private Button btnklantwijzigen;
    @FXML
    private Button btnklantverwijderen;
    @FXML
    private TableView<Klant> klantTable;

    @FXML
    public void changeSceneMain(ActionEvent event) throws IOException {
        WindowManager.getInstance().returnToMain();
    }

    @FXML
    public void addKlant(ActionEvent event) throws IOException, SQLException {
        Pair<AddKlantController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                .buildDialog(Resource.FXML_ADD_KLANT, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });

        Optional<ButtonType> result = dialogSet.getValue().showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            AddKlantController addKlantController = dialogSet.getKey();
            Klant klant = addKlantController.getNewKlant();
            // System.out.println(klant.getKlantnaam());
            if (klant.getKlantnaam().isEmpty()) {
                System.out.println("geen klantnaam ingevuld");
            } else {
                DataSource.getInstance().klantToevoegen(klant);
            }
        }

        refreshScreen();
    }

    @FXML
    public void updateKlant(ActionEvent event) throws IOException, SQLException {
        Klant klant2 = (Klant) klantTable.getSelectionModel().getSelectedItem();

        if (klant2 != null) {
            Pair<AddKlantController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_KLANT, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddKlantController addklantcontroller = dialogSet.getKey();
            addklantcontroller.editKlant(klant2, "update");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // System.out.println(klant2.getKlantnaam());
                if (klant2.getKlantnaam().isEmpty()) {
                    System.out.println("geen klantnaam ingevuld");
                } else {
                    addklantcontroller.updateKlant(klant2);
                    DataSource.getInstance().updateKlant(klant2);
                }
            }

            refreshScreen();
            updateView();
        }
    }

    @FXML
    public void deleteKlant(ActionEvent event) throws IOException, SQLException {
        Klant klant = (Klant) klantTable.getSelectionModel().getSelectedItem();
        // System.out.println(klant.getKlantnaam() + klant);

        if (klant != null) {
            Pair<AddKlantController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_KLANT, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddKlantController addklantcontroller = dialogSet.getKey();
            addklantcontroller.editKlant(klant, "delete");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (klant.getKlantnaam().isEmpty()) {
                    System.out.println("geen klantnaam ingevuld");
                } else {
                    // addklantcontroller.deleteKlant(klant);
                    DataSource.getInstance().deleteKlant(klant);
                }
            }
            refreshScreen();

        } else {
            System.out.println("geen klant selectie");
        }
        updateView();
    }

    @FXML
    protected void initialize() {
        listKlanten();
    }

    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Config.getInstance().windowWidth = (int) window.getWidth();
        updateView();
    }

    public void changelistener(final TableColumn<TableView<Klant>, String> listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double Kolumnwidthklantnaam = (columnklantnaam.widthProperty().getValue()) / 4.9;
                double Kolumnwidthklantcontactpersoon = (columnklantcontactpersoon.widthProperty().getValue()) / 4.9;
                double Kolumnwidthklantcontacttelnr = (columnklantcontacttelnr.widthProperty().getValue()) / 4.9;
                double Kolumnwidthklantcontactemail = (columnklantcontactemail.widthProperty().getValue()) / 4.9;
                double Kolumnwidthklantopmerking = (columnklantopmerking.widthProperty().getValue()) / 4.9;

                ObservableList<Klant> klantenlist = FXCollections
                        .observableArrayList(DataSource.getInstance().queryKlant());

                for (Klant huidigeklant : klantenlist) {
                    if (!(huidigeklant.getKlantnaam() == null))
                        huidigeklant.setKlantnaam(EditAanvraagController.lineWrap(huidigeklant.getKlantnaam(),
                                (int) Kolumnwidthklantnaam));
                    if (!(huidigeklant.getKlantcontactpersoon() == null)) {
                        huidigeklant.setKlantcontactpersoon(EditAanvraagController
                                .lineWrap(huidigeklant.getKlantcontactpersoon(), (int) Kolumnwidthklantcontactpersoon));
                    }
                    if (!(huidigeklant.getKlantcontacttelnr() == null)) {
                        huidigeklant.setKlantcontacttelnr(EditAanvraagController
                                .lineWrap(huidigeklant.getKlantcontacttelnr(), (int) Kolumnwidthklantcontacttelnr));
                    }
                    if (!(huidigeklant.getKlantcontactemail() == null)) {
                        huidigeklant.setKlantcontactemail(EditAanvraagController
                                .lineWrap(huidigeklant.getKlantcontactemail(), (int) Kolumnwidthklantcontactemail));
                    }
                    if (!(huidigeklant.getKlantopmerking() == null)) {
                        huidigeklant.setKlantopmerking(EditAanvraagController.lineWrap(huidigeklant.getKlantopmerking(),
                                (int) Kolumnwidthklantopmerking));
                    }

                    klantTable.itemsProperty().unbind();
                    klantTable.setItems(klantenlist);
                }
            }
        });
    }

    public void listKlanten() {
        Task<ObservableList<Klant>> task = new GetAllKlantenTask();
        klantTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @Override
    public void updateView() {
        if (klantTable.getSelectionModel().getSelectedItem() == null) {

            btnklanttoevoegen.setText("Toevoegen");
            btnklantwijzigen.setDisable(true);
            btnklantverwijderen.setDisable(true);
        } else {
            btnklantwijzigen.setDisable(false);
            btnklantverwijderen.setDisable(false);
        }

        changelistener(columnklantnaam);
        changelistener(columnklantcontactpersoon);
        changelistener(columnklantcontacttelnr);
        changelistener(columnklantcontactemail);
        changelistener(columnklantopmerking);
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_OVERZICHT_KLANT;
    }

    @Override
    public void refreshScreen() {
        ObservableList<Klant> Klantlist = FXCollections.observableArrayList(DataSource.getInstance().queryKlant());
        klantTable.itemsProperty().unbind();
        klantTable.setItems(Klantlist);
    }
}

class GetAllKlantenTask extends Task<ObservableList<Klant>> {

    @Override
    public ObservableList<Klant> call() {

        ObservableList<Klant> klantenList = FXCollections.observableArrayList(DataSource.getInstance().queryKlant());
        return klantenList;

    }
}

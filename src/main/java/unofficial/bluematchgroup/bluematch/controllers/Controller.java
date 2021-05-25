package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Aanbod;
import unofficial.bluematchgroup.bluematch.model.Aanvraag;
import unofficial.bluematchgroup.bluematch.model.DataSource;
import unofficial.bluematchgroup.bluematch.model.OverviewRecord;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class Controller implements IController {

    @FXML
    private TableColumn<TableView<OverviewRecord>, String> columnBroker;
    @FXML
    private TableColumn<TableView<OverviewRecord>, String> columnFunctie;
    @FXML
    private TableColumn<TableView<OverviewRecord>, String> columnStatusKlant;
    @FXML
    private TableColumn<TableView<OverviewRecord>, String> columnMedewerker;
    @FXML
    private TableColumn<TableView<OverviewRecord>, String> columnbrokernaam;
    @FXML
    private TableColumn<TableView<OverviewRecord>, String> columnContact;
    @FXML
    private TableView<OverviewRecord> overviewRecordTable;
    @FXML
    private TextField brokerTextField;
    @FXML
    private TextField medewerkerTextField;
    @FXML
    private ComboBox<String> statusKlantCombo;
    @FXML
    private Button aanbiedingmaken;
    @FXML
    private ComboBox<String> statusAanbiedingCombo;

    ObservableList<String> options = FXCollections.observableArrayList("", "Nieuw", "Vrijblijvend aangeboden",
            "Aangeboden Broker", "Aangeboden Eindklant", "Aangeboden"

    );

    ObservableList<String> optionsaanb = FXCollections.observableArrayList("", "Nieuw", "Uitgenodigd voor gesprek",
            "Plaatsing", "Afgewezen", "Teruggetrokken", "Overig");

    @FXML
    private void initialize() {
        statusKlantCombo.setItems(options);
        statusKlantCombo.setValue("");
        statusAanbiedingCombo.setItems(optionsaanb);
        statusAanbiedingCombo.setValue("");
    }

    @FXML
    private void refrfilter() {
        listOverviewRecord();
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
        // ** OLD CODE **
        // FXMLLoader loader = new FXMLLoader(getClass().getResource(Resource.FXML_OVERZICHT_BROKER.toString()));
        // Parent detailViewParent = loader.load();
        // OverzichtBrokerController ctrlbrokeroverzicht = loader.getController();
        // ctrlbrokeroverzicht.listBrokers();
        // Scene detailViewScene = new Scene(detailViewParent);
        // Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // window.setScene((detailViewScene));
        // window.show();
        // Config.getInstance().windowWidth = (int) window.getWidth();
        // window.widthProperty().addListener((obs, oldVal, newVal) -> {
        //     Config.getInstance().windowWidth = (int) window.getWidth();

        //     ctrlbrokeroverzicht.updateView();
        //     ctrlbrokeroverzicht.refreshScreen();
        //     // System.out.println("updated aanbod");
        // });
        // ctrlbrokeroverzicht.updateView();

    }

    @FXML
    public void overzichtKlant(ActionEvent event) throws IOException {
        WindowManager.getInstance().changeScene(Resource.FXML_OVERZICHT_KLANT);
    }

    @FXML
    public void aanbieden(ActionEvent event) throws IOException, SQLException {
        if (overviewRecordTable.getSelectionModel().getSelectedItem() != null) {
            Pair<AddAanbiedingController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance().buildDialog(Resource.FXML_ADD_AANBIEDING, new ButtonType[] {ButtonType.OK, ButtonType.CANCEL});
            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            {
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    AddAanbiedingController addAanbiedingController = dialogSet.getKey();
                    Aanbod aanbod = addAanbiedingController.getNewAanbod(
                            String.valueOf(overviewRecordTable.getSelectionModel().getSelectedItem().getIdaanvraag()));
                    DataSource.getInstance().aanbodToevoegen(aanbod);
                }
            }
            // ObservableList<OverviewRecord> Overviewlist =
            // FXCollections.observableArrayList(Datasource.getInstance().queryMain());
            // overviewRecordTable.itemsProperty().unbind();
            // overviewRecordTable.setItems(Overviewlist);
            refreshScreen();
            updateView();
        }
    }

    @FXML
    public void aanvraagToevoegen(ActionEvent event) throws IOException, SQLException {
        Pair<AddAanvraagController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance().buildDialog(Resource.FXML_ADD_AANVRAAG, new ButtonType[] {ButtonType.OK, ButtonType.CANCEL});
        Optional<ButtonType> result = dialogSet.getValue().showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddAanvraagController addaanvraagController = dialogSet.getKey();
            Aanvraag aanvraag = addaanvraagController.getNewAanvraag();
            DataSource.getInstance().aanvraagToevoegen(aanvraag);
        }
        refreshScreen();
        updateView();
    }

    @FXML
    public void changeSceneAanbodDetails(ActionEvent event) throws IOException {
        WindowManager.getInstance().changeScene(Resource.FXML_EDIT_AANBOD);
    }

    @FXML
    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        if (event.getClickCount() > 1) {
            // System.out.println("Table double clicked");
            // System.out.println(overviewRecordTable.getSelectionModel().getSelectedItem().getIdaanvraag());

            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resource.FXML_OVERZICHT_DETAILS.toString()));
            Parent detailViewParent = loader.load();
            OverzichtDetailsController ctrldetailsoverzicht = loader.getController();
            ctrldetailsoverzicht.listDetailsRecord(overviewRecordTable.getSelectionModel().getSelectedItem());
            Scene detailViewScene = new Scene(detailViewParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene((detailViewScene));
            window.show();
            // ctrldetailsoverzicht.updateView();

        }

        updateView();
    }

    public void listOverviewRecord() {
        Task<ObservableList<OverviewRecord>> task = new GetAllOverviewRecordTask();
        overviewRecordTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
        updateView();
    }

    private void changelistener(final TableColumn<TableView<OverviewRecord>, String> listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {

                double Kolumnwidthmedewerker = (columnMedewerker.widthProperty().getValue()) / 7;
                double Kolumnwidthbroker = (columnBroker.widthProperty().getValue()) / 7;
                double Kolumnwidthstatus = (columnStatusKlant.widthProperty().getValue()) / 7;
                double Kolumnwidthfunctie = (columnFunctie.widthProperty().getValue()) / 7;

                ObservableList<OverviewRecord> Overviewlist = FXCollections
                        .observableArrayList(DataSource.getInstance().queryMain());

                for (OverviewRecord huidigeoverviewrecord : Overviewlist) {
                    if (!(huidigeoverviewrecord.getRefbroker() == null))
                        huidigeoverviewrecord.setRefbroker(EditAanvraagController
                                .lineWrap(huidigeoverviewrecord.getRefbroker(), (int) Kolumnwidthbroker));
                    if (!(huidigeoverviewrecord.getMedewerker() == null)) {
                        huidigeoverviewrecord.setMedewerker(EditAanvraagController
                                .lineWrap(huidigeoverviewrecord.getMedewerker(), (int) Kolumnwidthmedewerker));
                    }
                    if (!(huidigeoverviewrecord.getFunctie() == null)) {
                        huidigeoverviewrecord.setFunctie(EditAanvraagController
                                .lineWrap(huidigeoverviewrecord.getFunctie(), (int) Kolumnwidthfunctie));
                    }
                    if (!(huidigeoverviewrecord.getStatusklant() == null)) {
                        huidigeoverviewrecord.setStatusklant(EditAanvraagController
                                .lineWrap(huidigeoverviewrecord.getStatusklant(), (int) Kolumnwidthstatus));
                    }
                    overviewRecordTable.itemsProperty().unbind();
                    overviewRecordTable.setItems(Overviewlist);
                }
            }
        });
    }

    @Override
    public void refreshScreen() {
        ObservableList<OverviewRecord> Overviewlist = FXCollections
                .observableArrayList(DataSource.getInstance().queryMain());
        overviewRecordTable.itemsProperty().unbind();
        overviewRecordTable.setItems(Overviewlist);
    }

    @Override
    public void updateView() {
        if (overviewRecordTable.getSelectionModel().getSelectedItem() == null) {

            aanbiedingmaken.setDisable(true);
        } else {

            aanbiedingmaken.setDisable(false);
        }

        DataSource.filterstatus = statusKlantCombo.getValue();
        DataSource.filterstatusaanb = statusAanbiedingCombo.getValue();
        DataSource.filterbroker = brokerTextField.getText();
        DataSource.filtermedewerker = medewerkerTextField.getText();

        changelistener(columnBroker);
        changelistener(columnFunctie);
        changelistener(columnMedewerker);
        changelistener(columnFunctie);

        // changelistenerstatus(columnStatusKlant);
        // ObservableList<OverviewRecord> Overviewlist = FXCollections
        //        .observableArrayList(DataSource.getInstance().queryMain());
        // Overviewlist.addListener((Change<? extends OverviewRecord> c) -> { });
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_MAIN;
    }
}

class GetAllOverviewRecordTask extends Task<ObservableList<OverviewRecord>> {
    @Override
    public ObservableList<OverviewRecord> call() {
        return FXCollections.observableArrayList(DataSource.getInstance().queryMain());
    }
}

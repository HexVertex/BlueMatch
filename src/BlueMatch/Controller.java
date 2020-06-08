package BlueMatch;

import BlueMatch.model.Aanbod;
import BlueMatch.model.Aanvraag;
import BlueMatch.model.Datasource;
import BlueMatch.model.OverviewRecord;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class Controller {

    @FXML
    private TableColumn columnBroker;
    @FXML
    private TableColumn columnFunctie;
    @FXML
    private TableColumn columnStatusKlant;
    @FXML
    private TableColumn columnMedewerker;
    @FXML
    private TableColumn columnbrokernaam;
    @FXML
    private TableColumn columnContact;
    @FXML
    private TableView<OverviewRecord> overviewRecordTable;

    @FXML
    private TextField brokerTextField;

    @FXML
    private TextField medewerkerTextField;

    @FXML
    private ComboBox<String> statusKlantCombo;

    ObservableList<String> options =
            FXCollections.observableArrayList(
                    "", "Nieuw",
                    "Vrijblijvend aangeboden",
                    "Aangeboden Broker",
                    "Aangeboden Eindklant",
                    "Aangeboden"


            );
    @FXML
    private ComboBox<String> statusAanbiedingCombo;

    ObservableList<String> optionsaanb =
            FXCollections.observableArrayList(
                    "",
                    "Nieuw",
                    "Uitgenodigd voor gesprek",
                    "Plaatsing",
                    "Afgewezen",
                    "Teruggetrokken",
                    "Overig"
            );

    @FXML
    private void initialize() {
        statusKlantCombo.setItems(options);
        statusKlantCombo.setValue("");
        statusAanbiedingCombo.setItems(optionsaanb);
        statusAanbiedingCombo.setValue("");
    }

    public void listOverviewRecord() {
        Task<ObservableList<OverviewRecord>> task = new GetAllOverviewRecordTask();
        overviewRecordTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
        updateMainView();
    }

    @FXML
    private void refrfilter () {
        listOverviewRecord();
    }

    @FXML
    public void changeSceneAanvraagDetails(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("editaanvrg.fxml"));
        Parent detailViewParent = loader.load();
        Editaanvraag ctrleditaanvraag = loader.getController();

        ctrleditaanvraag.listAanvragen();

        Scene detailViewScene = new Scene(detailViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();


        ctrleditaanvraag.setParentScene(window.getScene());
        ctrleditaanvraag.setParentController(this);
        window.setScene((detailViewScene));
        window.show();
        ctrleditaanvraag.updateView();
    }

    @FXML
    public void overzichtMedewerker(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Medewerkeroverzicht.fxml"));
        Parent detailViewParent = loader.load();
        MedewerkerOverzicht ctrlmdwoverzicht = loader.getController();
        ctrlmdwoverzicht.listMedewerkers();
        Scene detailViewScene = new Scene(detailViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ctrlmdwoverzicht.setParentScene(window.getScene());
        ctrlmdwoverzicht.setParentController(this);
        window.setScene((detailViewScene));
        window.show();
        ctrlmdwoverzicht.updateView();
    }
    @FXML
    public void overzichtBroker(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Brokeroverzicht.fxml"));
        Parent detailViewParent = loader.load();
        BrokerOverzicht ctrlbrokeroverzicht = loader.getController();
        ctrlbrokeroverzicht.listBrokers();
        Scene detailViewScene = new Scene(detailViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ctrlbrokeroverzicht.setParentScene(window.getScene());
        ctrlbrokeroverzicht.setParentController(this);
        window.setScene((detailViewScene));
        window.show();
        ctrlbrokeroverzicht.updateView();
    }
    @FXML
    public void overzichtKlant(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Klantoverzicht.fxml"));
        Parent detailViewParent = loader.load();
        KlantOverzicht ctrlklantoverzicht = loader.getController();
        ctrlklantoverzicht.listKlanten();
        Scene detailViewScene = new Scene(detailViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ctrlklantoverzicht.setParentScene(window.getScene());
        ctrlklantoverzicht.setParentController(this);
        window.setScene((detailViewScene));
        window.show();
        ctrlklantoverzicht.updateView();
    }
    @FXML
    public void aanbieden(ActionEvent event) throws IOException, SQLException {
        if (overviewRecordTable.getSelectionModel().getSelectedItem() != null) {
            // System.out.println(overviewRecordTable.getSelectionModel().getSelectedItem().getRefbroker());
            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addaanbieding.fxml"));
            dialog.getDialogPane().setContent(loader.load());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Optional<ButtonType> result = dialog.showAndWait();
            {
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    addAanbiedingController addAanbiedingController = loader.getController();
                    Aanbod aanbod = addAanbiedingController.getNewAanbod(overviewRecordTable.getSelectionModel().getSelectedItem().getIdaanvraag());
                    Datasource.getInstance().aanbodToevoegen(aanbod);
                }
            }
            ObservableList<OverviewRecord> Overviewlist = FXCollections.observableArrayList(Datasource.getInstance().queryMain());
            overviewRecordTable.itemsProperty().unbind();
            overviewRecordTable.setItems(Overviewlist);
            updateMainView();
        }
    }

    @FXML
    public void aanvraagToevoegen(ActionEvent event) throws IOException, SQLException {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addaanvraag.fxml"));
        dialog.getDialogPane().setContent(loader.load());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AddAanvraagController addaanvraagController = loader.getController();
                Aanvraag aanvraag = addaanvraagController.getNewAanvraag();
                Datasource.getInstance().aanvraagToevoegen(aanvraag);
            }
        }
        ObservableList<OverviewRecord> Overviewlist = FXCollections.observableArrayList(Datasource.getInstance().queryMain());
        overviewRecordTable.itemsProperty().unbind();
        overviewRecordTable.setItems(Overviewlist);
        updateMainView();
    }


    @FXML
    public void changeSceneAanbodDetails(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("editaanbod.fxml"));
        Parent detailViewParent = loader.load();
        Editaanbod ctrleditaanbod = loader.getController();

        ctrleditaanbod.listAanbiedingen();
        Scene detailViewScene = new Scene(detailViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ctrleditaanbod.setParentScene(window.getScene());
        ctrleditaanbod.setParentController(this);
        window.setScene((detailViewScene));
        window.show();
        window.widthProperty().addListener((obs, oldVal, newVal) -> {
            Main.windowWidth = (int) window.getWidth();
            // System.out.println(newVal);
            ctrleditaanbod.updateView();
        });


    }


    @FXML
    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        // resize table on mouse clicked
        updateMainView();
    }


    public void updateMainView() {

        Datasource.filterstatus=statusKlantCombo.getValue();
        Datasource.filterstatusaanb=statusAanbiedingCombo.getValue();
        Datasource.filterbroker=brokerTextField.getText();
        Datasource.filtermedewerker=medewerkerTextField.getText();
        System.out.println(Datasource.filterstatus);

        changelistener(columnBroker);
        changelistener(columnFunctie);
        changelistener(columnMedewerker);
        changelistener(columnFunctie);

    }

    public void changelistener(final TableColumn listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // System.out.print(listerColumn.getText() + "  ");
                // System.out.println(t1);

                double Kolumnwidthmedewerker = (columnMedewerker.widthProperty().getValue()) / 7;
                double Kolumnwidthbroker = (columnBroker.widthProperty().getValue()) / 7;
                double Kolumnwidthstatus = (columnStatusKlant.widthProperty().getValue()) / 7;
                double Kolumnwidthfunctie = (columnFunctie.widthProperty().getValue()) / 7;

                ObservableList<OverviewRecord> Overviewlist = FXCollections.observableArrayList(Datasource.getInstance().queryMain());

                System.out.println("gefilterd op: " + Datasource.filterstatus);
                for (OverviewRecord huidigeoverviewrecord : Overviewlist) {
                    if (!(huidigeoverviewrecord.getRefbroker() == null))
                        huidigeoverviewrecord.setRefbroker(Editaanvraag.lineWrap(huidigeoverviewrecord.getRefbroker(), (int) Kolumnwidthbroker));
                    if (!(huidigeoverviewrecord.getMedewerker() == null)) {
                        huidigeoverviewrecord.setMedewerker(Editaanvraag.lineWrap(huidigeoverviewrecord.getMedewerker(), (int) Kolumnwidthmedewerker));
                    }
                    if (!(huidigeoverviewrecord.getFunctie() == null)) {
                        huidigeoverviewrecord.setFunctie(Editaanvraag.lineWrap(huidigeoverviewrecord.getFunctie(), (int) Kolumnwidthfunctie));
                    }
                    if (!(huidigeoverviewrecord.getStatusKlant() == null)) {
                        huidigeoverviewrecord.setStatusKlant(Editaanvraag.lineWrap(huidigeoverviewrecord.getStatusKlant(), (int) Kolumnwidthstatus));
                    }
                    overviewRecordTable.itemsProperty().unbind();
                    overviewRecordTable.setItems(Overviewlist);
                }
            }
        });
    }
}

class GetAllOverviewRecordTask extends Task {
    @Override
    public ObservableList<OverviewRecord> call() {
        System.out.println("Getalloverview filter" + Datasource.filterstatus);
        return FXCollections.observableArrayList(Datasource.getInstance().queryMain());
    }
}

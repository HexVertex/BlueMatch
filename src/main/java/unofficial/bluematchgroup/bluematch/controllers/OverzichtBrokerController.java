package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Config;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Broker;
import unofficial.bluematchgroup.bluematch.model.DataSource;

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

//import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class OverzichtBrokerController implements IController {

    @FXML
    private TableColumn<TableView<Broker>, String> columnbrokernaam;
    @FXML
    private TableColumn<TableView<Broker>, String> columncontactpersoon;
    @FXML
    private TableColumn<TableView<Broker>, String> columntelbroker;
    @FXML
    private TableColumn<TableView<Broker>, String> columnemailbroker;
    @FXML
    private TableColumn<TableView<Broker>, String> columnopmerkingbroker;
    @FXML
    private Button btnmodbroker;
    @FXML
    private TableView<Broker> brokerTable;

    @FXML
    public void changeSceneMain(ActionEvent event) throws IOException {
        WindowManager.getInstance().returnToMain();
    }

    @FXML
    public void addBroker(ActionEvent event) throws IOException, SQLException {

        Pair<AddBrokerController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_BROKER, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
        Optional<ButtonType> result = dialogSet.getValue().showAndWait();
        {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AddBrokerController addBrokerController = dialogSet.getKey();
                Broker broker = addBrokerController.getNewBroker();
                DataSource.getInstance().brokerToevoegen(broker);
            }
        }

        refreshScreen();
    }

    @FXML
    public void deleteBroker(ActionEvent event) throws IOException, SQLException {
        Broker broker = (Broker) brokerTable.getSelectionModel().getSelectedItem();

        if (broker != null) {
            Pair<AddBrokerController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_BROKER, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddBrokerController addbrokercontroller = dialogSet.getKey();
            addbrokercontroller.editBroker(broker, "delete");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (broker.getBrokernaam().isEmpty()) {
                    System.out.println("geen brokernaam ingevuld");
                } else {
                    // addbrokercontroller.deleteBroker(broker);
                    DataSource.getInstance().deleteBroker(broker);
                }
            }
            refreshScreen();

        } else {
            System.out.println("geen broker selectie");
        }
    }

    @FXML
    public void updateBroker(ActionEvent event) throws IOException, SQLException {
        Broker broker = (Broker) brokerTable.getSelectionModel().getSelectedItem();

        if (broker != null) {
            Pair<AddBrokerController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_BROKER, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddBrokerController addbrokercontroller = dialogSet.getKey();
            addbrokercontroller.editBroker(broker, "update");

            // System.out.println(broker.getIdbroker());
            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (broker.getBrokernaam().isEmpty()) {
                    System.out.println("geen brokernaam ingevuld");
                } else {
                    addbrokercontroller.updateBroker(broker);
                    DataSource.getInstance().updateBroker(broker);
                }
            }
            refreshScreen();
            btnmodbroker.setDisable(true);
        }
    }

    @FXML
    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Config.getInstance().windowWidth = (int) window.getWidth();
        // ObservableList<Broker> brokerslist = FXCollections.observableArrayList(DataSource.getInstance().queryBroker());
        updateView();
    }

    @FXML
    public void initialize() {
        this.listBrokers();
    }

    void listBrokers() {
        Task<ObservableList<Broker>> task = new GetAllBrokersTask();
        brokerTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    private void changelistener(final TableColumn<TableView<Broker>, String> listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double Kolumnwidthbrokernaam = (columnbrokernaam.widthProperty().getValue()) / 4.9;
                double Kolumnwidthcontactpersoon = (columncontactpersoon.widthProperty().getValue()) / 4.9;
                double Kolumnwidthtelbroker = (columntelbroker.widthProperty().getValue()) / 4.9;
                double Kolumnwidthemailbroker = (columnemailbroker.widthProperty().getValue()) / 4.9;
                double Kolumnwidthopmerkingbroker = (columnemailbroker.widthProperty().getValue()) / 4.9;

                ObservableList<Broker> brokerslist = FXCollections
                        .observableArrayList(DataSource.getInstance().queryBroker());

                for (Broker huidigebroker : brokerslist) {
                    if (!(huidigebroker.getBrokernaam() == null))
                        huidigebroker.setBrokernaam(EditAanvraagController.lineWrap(huidigebroker.getBrokernaam(),
                                (int) Kolumnwidthbrokernaam));
                    if (!(huidigebroker.getContactpersoon() == null)) {
                        huidigebroker.setContactpersoon(EditAanvraagController
                                .lineWrap(huidigebroker.getContactpersoon(), (int) Kolumnwidthcontactpersoon));
                    }
                    if (!(huidigebroker.getTelbroker() == null)) {
                        huidigebroker.setTelbroker(EditAanvraagController.lineWrap(huidigebroker.getTelbroker(),
                                (int) Kolumnwidthtelbroker));
                    }
                    if (!(huidigebroker.getEmailbroker() == null)) {
                        huidigebroker.setEmailbroker(EditAanvraagController.lineWrap(huidigebroker.getEmailbroker(),
                                (int) Kolumnwidthemailbroker));
                    }
                    if (!(huidigebroker.getOpmerkingbroker() == null)) {
                        huidigebroker.setOpmerkingbroker(EditAanvraagController
                                .lineWrap(huidigebroker.getOpmerkingbroker(), (int) Kolumnwidthopmerkingbroker));
                    }

                    brokerTable.itemsProperty().unbind();
                    brokerTable.setItems(brokerslist);
                }
            }
        });
    }

    @Override
    public void refreshScreen() {
        ObservableList<Broker> Brokerlist = FXCollections.observableArrayList(DataSource.getInstance().queryBroker());
        brokerTable.itemsProperty().unbind();
        brokerTable.setItems(Brokerlist);
    }

    @Override
    public void updateView() {
        if (brokerTable.getSelectionModel().getSelectedItem() == null) {
            // System.out.println("brokertable not selected");
            btnmodbroker.setDisable(true);
        } else {
            // System.out.println("brokertable selected");
            btnmodbroker.setDisable(false);
        }

        changelistener(columnbrokernaam);
        changelistener(columncontactpersoon);
        changelistener(columntelbroker);
        changelistener(columnemailbroker);
        // changelistener(columnopmerkingbroker);
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_OVERZICHT_BROKER;
    }
}

class GetAllBrokersTask extends Task<ObservableList<Broker>> {

    @Override
    public ObservableList<Broker> call() {

        ObservableList<Broker> BrokersList = FXCollections.observableArrayList(DataSource.getInstance().queryBroker());
        return BrokersList;

    }
}

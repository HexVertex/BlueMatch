package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Config;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.DataSource;
import unofficial.bluematchgroup.bluematch.model.Medewerker;
import unofficial.bluematchgroup.bluematch.procedures.SendEmailOffice365;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class OverzichtMedewerkerController implements IController {

    @FXML
    private TableColumn<TableView<Medewerker>, String> columnvoornaam;
    @FXML
    private TableColumn<TableView<Medewerker>, String> columnachternaam;
    @FXML
    private TableColumn<TableView<Medewerker>, String> columnurenperweek;
    @FXML
    private TableColumn<TableView<Medewerker>, String> columnstatusmdw;
    @FXML
    private Button BtnModMedewerker;
    @FXML
    private Button BtnSendMail;
    @FXML
    private Button BtnDelMedewerker;
    @FXML
    private TableView<Medewerker> medewerkerTable;
    @FXML
    private PasswordField passwordmail;

    @FXML
    public void changeSceneMain(ActionEvent event) {
        WindowManager.getInstance().returnToMain();
    }

    @FXML
    public void sendmail(ActionEvent event) {
        Medewerker medewerker = medewerkerTable.getSelectionModel().getSelectedItem();
        String destination = medewerker.getEmailmedewerker();
        System.out.println("send mail");
        new SendEmailOffice365().sendEmail(passwordmail, destination);
    }

    @FXML
    public void addMedewerker(ActionEvent event) throws IOException, SQLException {
        // System.out.println("add medewerker");

        Pair<AddMedewerkerController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                .buildDialog(Resource.FXML_ADD_MEDEWERKER, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
        Optional<ButtonType> result = dialogSet.getValue().showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddMedewerkerController addMedewerkerController = dialogSet.getKey();
            Medewerker medewerker = addMedewerkerController.getNewMedewerker();
            DataSource.getInstance().medewerkerToevoegen(medewerker);
        }
        // updateMainView();
        refreshScreen();
        updateView();

    }

    @FXML
    public void updateMedewerker(ActionEvent event) throws IOException {
        Medewerker medewerker = medewerkerTable.getSelectionModel().getSelectedItem();

        if (medewerker != null) {
            Pair<AddMedewerkerController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_MEDEWERKER, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddMedewerkerController addmedewerkercontroller = dialogSet.getKey();
            addmedewerkercontroller.editMedewerker(medewerker, "update");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (medewerker.getVoornaam().isEmpty()) {
                    System.out.println("geen medewerkernaam ingevuld");
                } else {
                    addmedewerkercontroller.updateMedewerker(medewerker);
                    DataSource.getInstance().updateMedewerker(medewerker);
                }
            }
            refreshScreen();
            BtnModMedewerker.setDisable(true);
        } else {
            System.out.println("geen medewerker selectie");
        }
    }

    @FXML
    public void deleteMedewerker(ActionEvent event) throws IOException {
        Medewerker medewerker = medewerkerTable.getSelectionModel().getSelectedItem();

        if (medewerker != null) {
            Pair<AddMedewerkerController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_MEDEWERKER, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddMedewerkerController addmedewerkercontroller = dialogSet.getKey();
            addmedewerkercontroller.editMedewerker(medewerker, "delete");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (medewerker.getVoornaam().isEmpty()) {
                    System.out.println("geen medewerkernaam ingevuld");
                } else {
                    // addmedewerkercontroller.deleteMedewerker(medewerker);
                    DataSource.getInstance().deleteMedewerker(medewerker);
                }
            }
            refreshScreen();
            BtnModMedewerker.setDisable(true);
        } else {
            System.out.println("geen medewerker selectie");
        }
    }

    @FXML
    public void tableViewMouseClicked(MouseEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Config.getInstance().windowWidth = (int) window.getWidth();
        updateView();
    }

    @FXML
    public void initialize() {
        this.listMedewerkers();
    }

    private void changelistener(final TableColumn<TableView<Medewerker>, String> listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double Kolumnwidthvoornaam = (columnvoornaam.widthProperty().getValue()) / 4.9;
                double Kolumnwidthachternaam = (columnachternaam.widthProperty().getValue()) / 4.9;
                double Kolumnwidthstatusmdw = (columnstatusmdw.widthProperty().getValue()) / 4.9;
                double Kolumnwidthurenperweek = (columnurenperweek.widthProperty().getValue()) / 4.9;

                ObservableList<Medewerker> medewerkerslist = FXCollections
                        .observableArrayList(DataSource.getInstance().queryMedewerker());

                for (Medewerker huidigemdw : medewerkerslist) {
                    if (!(huidigemdw.getVoornaam() == null))
                        huidigemdw.setVoornaam(
                                EditAanvraagController.lineWrap(huidigemdw.getVoornaam(), (int) Kolumnwidthvoornaam));
                    if (!(huidigemdw.getAchternaam() == null)) {
                        huidigemdw.setAchternaam(EditAanvraagController.lineWrap(huidigemdw.getAchternaam(),
                                (int) Kolumnwidthachternaam));
                    }
                    if (!(huidigemdw.getUrenperweek() == null)) {
                        huidigemdw.setUren(EditAanvraagController.lineWrap(huidigemdw.getUrenperweek(),
                                (int) Kolumnwidthurenperweek));
                    }
                    if (!(huidigemdw.getStatusmdw() == null)) {
                        huidigemdw.setStatusmdw(
                                EditAanvraagController.lineWrap(huidigemdw.getStatusmdw(), (int) Kolumnwidthstatusmdw));
                    }

                    medewerkerTable.itemsProperty().unbind();
                    medewerkerTable.setItems(medewerkerslist);
                }
            }
        });
    }

    void listMedewerkers() {
        GetAllMedewerkersTask task = new GetAllMedewerkersTask();
        medewerkerTable.itemsProperty().bind(task.valueProperty());
        // medewerkerTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @Override
    public void updateView() {
        if (medewerkerTable.getSelectionModel().getSelectedItem() == null) {

            BtnModMedewerker.setDisable(true);
            BtnSendMail.setDisable(true);
            BtnDelMedewerker.setDisable(true);
        } else {

            BtnModMedewerker.setDisable(false);
            if (!passwordmail.getText().isEmpty()) {
                BtnSendMail.setDisable(false);
            }
            BtnDelMedewerker.setDisable(false);
        }

        changelistener(columnvoornaam);
        changelistener(columnachternaam);
        changelistener(columnstatusmdw);
        changelistener(columnurenperweek);
        // changelistener(columnopmerkingbroker);
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_OVERZICHT_MEDEWERKER;
    }

    @Override
    public void refreshScreen() {
        ObservableList<Medewerker> Medewerkerlist = FXCollections
                .observableArrayList(DataSource.getInstance().queryMedewerker());
        medewerkerTable.itemsProperty().unbind();
        medewerkerTable.setItems(Medewerkerlist);
    }
}

class GetAllMedewerkersTask extends Task<ObservableList<Medewerker>> {

    @Override
    public ObservableList<Medewerker> call() {
        // ObservableList<Medewerker> medewerkersList =
        // FXCollections.observableArrayList(Datasource.getInstance().queryMedewerker());
        return FXCollections.observableArrayList(DataSource.getInstance().queryMedewerker());

    }
}

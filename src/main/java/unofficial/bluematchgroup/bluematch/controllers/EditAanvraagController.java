package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Aanvraag;
import unofficial.bluematchgroup.bluematch.model.DataSource;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditAanvraagController extends BaseController {

    @FXML
    private TableColumn<TableView<Aanvraag>, String> columnidaanvraag;
    @FXML
    private TableColumn<TableView<Aanvraag>, String> columnopm;
    @FXML
    private TableColumn<TableView<Aanvraag>, String> columnfunctie;
    @FXML
    private TableColumn<TableView<Aanvraag>, String> columnlocatie;
    @FXML
    private TableColumn<TableView<Aanvraag>, String> columnlink;
    @FXML
    private TableColumn<TableView<Aanvraag>, String> columnstatus;
    @FXML
    private TableColumn<TableView<Aanvraag>, String> columncontact;
    @FXML
    private Button btnmodaanvraag;
    @FXML
    private Button btndelaanvraag;
    @FXML
    private TableView<Aanvraag> aanvraagTable;

    @FXML
    public void changeSceneMain(ActionEvent event) throws IOException {
        WindowManager.getInstance().returnToMain();
    }

    @FXML
    public void addAanvraag(ActionEvent event) throws IOException, SQLException {
        // System.out.println("add aanvraag");
        Pair<AddAanvraagController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                .buildDialog(Resource.FXML_ADD_AANVRAAG, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
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
    public void modAanvraag(ActionEvent event) throws IOException, SQLException {
        Aanvraag aanvraag = (Aanvraag) aanvraagTable.getSelectionModel().getSelectedItem();

        if (aanvraag != null) {
            Pair<AddAanvraagController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_AANVRAAG, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddAanvraagController addaanvraagcontroller = dialogSet.getKey();
            addaanvraagcontroller.editAanvraag(aanvraag, "update");

            // System.out.println(aanvraag.getIdaanvraag());
            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                addaanvraagcontroller.updateAanvraag(aanvraag);
                DataSource.getInstance().updateAanvraag(aanvraag);
            }
        }
        refreshScreen();
        btnmodaanvraag.setDisable(true);
    }

    @FXML
    public void deleteAanvraag(ActionEvent event) throws IOException, SQLException {
        Aanvraag aanvraag = (Aanvraag) aanvraagTable.getSelectionModel().getSelectedItem();
        //System.out.println(aanvraag.getIdaanvraag());

        if (aanvraag != null) {
            Pair<AddAanvraagController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_AANVRAAG, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddAanvraagController addaanvraagcontroller = dialogSet.getKey();
            addaanvraagcontroller.editAanvraag(aanvraag, "delete");

            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (aanvraag.getIdaanvraag() == 0) {
                    System.out.println("geen aanvraag met id bekend");
                } else {
                    // addaanvraagcontroller.deleteAanvraag(aanvraag);
                    DataSource.getInstance().deleteAanvraag(aanvraag);
                }
            }
            this.refreshScreen();

        } else {
            System.out.println("geen aanvraag selectie");
        }
        updateView();
    }

    @FXML
    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        // resize table on mouse clicked
        updateView();
    }

    @FXML
    public void initialize() {
        this.listAanvragen();
    }

    public void changelistener(final TableColumn<TableView<Aanvraag>, String> listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                double Kolumnwidthopm = (columnopm.widthProperty().getValue()) / 7; // 4.9 is amount of pixels/4.9=
                                                                                    // amount of characters
                double Kolumnwidthfunctie = (columnfunctie.widthProperty().getValue()) / 7;
                double Kolumnwidthcontact = (columncontact.widthProperty().getValue()) / 7;
                // double Kolumnwidthstatus = (columnstatus.widthProperty().getValue()) / 7;
                double Kolumnwidthlocatie = (columnlocatie.widthProperty().getValue()) / 7;
                double Kolumnwidthlink = (columnlink.widthProperty().getValue()) / 7;

                ObservableList<Aanvraag> aanvraaglist = FXCollections
                        .observableArrayList(DataSource.getInstance().queryAanvraag());

                for (Aanvraag huidigeaanvraag : aanvraaglist) {
                    if (!(huidigeaanvraag.getOpmerking() == null)) {
                        huidigeaanvraag.setOpmerking(lineWrap(huidigeaanvraag.getOpmerking(), (int) Kolumnwidthopm));
                    }
                    if (!(huidigeaanvraag.getFunctie() == null)) {
                        huidigeaanvraag.setFunctie(lineWrap(huidigeaanvraag.getFunctie(), (int) Kolumnwidthfunctie));
                    }
                    if (!(huidigeaanvraag.getRefcontact() == null)) {
                        huidigeaanvraag
                                .setRefcontact(lineWrap(huidigeaanvraag.getRefcontact(), (int) Kolumnwidthcontact));
                    }
                    // if (!(huidigeaanvraag.getStatusklant() == null)) {
                    // huidigeaanvraag.setStatusklant(lineWrap(huidigeaanvraag.getStatusklant(),
                    // (int) Kolumnwidthstatus));
                    // }
                    if (!(huidigeaanvraag.getLocatie() == null)) {
                        huidigeaanvraag.setLocatie(lineWrap(huidigeaanvraag.getLocatie(), (int) Kolumnwidthlocatie));
                    }
                    if (!(huidigeaanvraag.getLinkaanvraag() == null)) {
                        huidigeaanvraag
                                .setLinkaanvraag(lineWrap(huidigeaanvraag.getLinkaanvraag(), (int) Kolumnwidthlink));
                    }
                    aanvraagTable.itemsProperty().unbind();
                    aanvraagTable.setItems(aanvraaglist);
                }

            }

        });

    }

    public static String lineWrap(String text, int limit) {
        int offset = 0;
        Pattern pattern = Pattern.compile(" ");
        StringBuilder result = new StringBuilder(text.length());
        while (offset < text.length()) {
            int wrapPosition = -1;
            Matcher matcher = pattern.matcher(text.substring(offset,
                    Math.min((int) Math.min(Integer.MAX_VALUE, offset + limit + 1L), text.length())));
            if (matcher.find()) {
                // Skip unnecessary space at the beginning of each line
                if (matcher.start() == 0) {
                    offset += matcher.end();
                    continue;
                }
                wrapPosition = matcher.start() + offset;
            }
            // Stop wrapping if the remaining words are within boundary
            if (text.length() - offset <= limit) {
                break;
            }
            // Find the position to insert line break
            while (matcher.find()) {
                wrapPosition = matcher.start() + offset;
            }
            if (wrapPosition >= offset) {
                // Normal case
                result.append(text, offset, wrapPosition).append(System.lineSeparator());
                offset = wrapPosition + 1;
            } else {
                // Word too long
                result.append(text, offset, offset + limit).append(System.lineSeparator());
                offset += limit;
            }
        }
        // Append the remaining words
        result.append(text, offset, text.length());
        return result.toString();
    }

    public void listAanvragen() {
        Task<ObservableList<Aanvraag>> task = new GetAllAanvragenTask();
        aanvraagTable.itemsProperty().bind(task.valueProperty());
        // System.out.println("View list on screen 2");
        new Thread(task).start();
    }

    @Override
    public void updateView() {
        if (aanvraagTable.getSelectionModel().getSelectedItem() == null) {
            btnmodaanvraag.setDisable(true);
            btndelaanvraag.setDisable(true);
        } else {
            btnmodaanvraag.setDisable(false);
            btndelaanvraag.setDisable(false);
        }

        changelistener(columnopm);
        changelistener(columnfunctie);
        changelistener(columncontact);
        changelistener(columnstatus);
        changelistener(columnlocatie);
        changelistener(columnlink);
    }

    @Override
    public void refreshScreen() {
        ObservableList<Aanvraag> Aanvraaglist = FXCollections
                .observableArrayList(DataSource.getInstance().queryAanvraag());
        aanvraagTable.itemsProperty().unbind();
        aanvraagTable.setItems(Aanvraaglist);
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_EDIT_AANVRAAG;
    }
}

class GetAllAanvragenTask extends Task<ObservableList<Aanvraag>> {

    @Override
    public ObservableList<Aanvraag> call() {

        ObservableList<Aanvraag> aanvraaglist = FXCollections
                .observableArrayList(DataSource.getInstance().queryAanvraag());
        // System.out.println(aanvraaglist.size());
        return aanvraaglist;

    }
}

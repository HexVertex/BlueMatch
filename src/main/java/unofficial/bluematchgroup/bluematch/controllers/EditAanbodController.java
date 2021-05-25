package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.WindowManager;
import unofficial.bluematchgroup.bluematch.config.Config;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.model.Aanbod;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class EditAanbodController implements IController {

    @FXML
    private TableColumn<TableView<Aanbod>, String> columnmedewerker;
    @FXML
    private TableColumn<TableView<Aanbod>, String> columntariefaanbod;
    @FXML
    private TableColumn<TableView<Aanbod>, String> columnaanbodstatus;
    @FXML
    private Button btnmodaanbod;
    @FXML
    private Button btndelaanbod;
    @FXML
    private TableView<Aanbod> aanbodTable;

    @FXML
    public void changeSceneMain(ActionEvent event) throws IOException {
        WindowManager.getInstance().returnToMain();
    }

    @FXML
    public void modAanbod(ActionEvent event) throws IOException, SQLException {
        // System.out.println("bijwerken aanbod");
        Aanbod aanbod = (Aanbod) aanbodTable.getSelectionModel().getSelectedItem();
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
            refreshScreen();
            updateView();
        }

    }

    @FXML
    public void deleteAanbod(ActionEvent event) throws IOException, SQLException {
        Aanbod aanbod = (Aanbod) aanbodTable.getSelectionModel().getSelectedItem();
        // System.out.println(aanbod.getAanbodnaam() + aanbod);

        if (aanbod != null) {
            Pair<AddAanbiedingController, Dialog<ButtonType>> dialogSet = WindowManager.getInstance()
                    .buildDialog(Resource.FXML_ADD_AANBIEDING, new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
            AddAanbiedingController addaanbodcontroller = dialogSet.getKey();
            addaanbodcontroller.editAanbod(aanbod, "delete");
            Optional<ButtonType> result = dialogSet.getValue().showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (aanbod.getRefaanvraag().isEmpty()) {
                    System.out.println("geen aanbodnaam ingevuld");
                } else {
                    // addaanbodcontroller.deleteAanbod(aanbod);
                    DataSource.getInstance().deleteAanbod(aanbod);
                }
            }
            refreshScreen();

        } else {
            System.out.println("geen aanbod selectie");
        }
        updateView();
    }

    @FXML
    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Config.getInstance().windowWidth = (int) window.getWidth();
        updateView();
    }

    @FXML
    public void initialize() {
        this.listAanbiedingen();
    }

    public void changelistener(final TableColumn<TableView<Aanbod>, String> listerColumn) {
        listerColumn.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double Kolumnwidthmedewerker = (columnmedewerker.widthProperty().getValue()) / 4.9;
                double Kolumnwidthtarief = (columntariefaanbod.widthProperty().getValue()) / 4.9;
                double Kolumnwidthstatus = (columnaanbodstatus.widthProperty().getValue()) / 4.9;
                // double Kolumnwidthlocatie = (columnlocatie.widthProperty().getValue()) / 4.9;

                ObservableList<Aanbod> aanbodlist = FXCollections
                        .observableArrayList(DataSource.getInstance().queryAanbod());

                for (Aanbod huidigeaanbod : aanbodlist) {
                    if (!(huidigeaanbod.getTariefaanbod() == null))
                        huidigeaanbod.setTariefaanbod(EditAanvraagController.lineWrap(huidigeaanbod.getTariefaanbod(),
                                (int) Kolumnwidthtarief));
                    if (!(huidigeaanbod.getRefmedewerker() == null)) {
                        huidigeaanbod.setRefmedewerker(EditAanvraagController.lineWrap(huidigeaanbod.getRefmedewerker(),
                                (int) Kolumnwidthmedewerker));
                    }
                    if (!(huidigeaanbod.getStatusaanbod() == null)) {
                        huidigeaanbod.setStatusaanbod(EditAanvraagController.lineWrap(huidigeaanbod.getStatusaanbod(),
                                (int) Kolumnwidthstatus));
                    }

                    aanbodTable.itemsProperty().unbind();
                    aanbodTable.setItems(aanbodlist);
                }
            }
        });
    }

    public void listAanbiedingen() {
        Task<ObservableList<Aanbod>> task = new GetAllAanbiedingenTask();
        aanbodTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @Override
    public void updateView() {
        if (aanbodTable.getSelectionModel().getSelectedItem() == null) {
            System.out.println("aanbodtable not selected");
            btnmodaanbod.setDisable(true);
            btndelaanbod.setDisable(true);
        } else {
            System.out.println("aanbodtable selected");
            btnmodaanbod.setDisable(false);
            btndelaanbod.setDisable(false);
        }

        changelistener(columnmedewerker);
        changelistener(columntariefaanbod);
        changelistener(columnaanbodstatus);

        // changelistener(columnopmerkingbroker);
    }

    @Override
    public Resource getFxml() {
        return Resource.FXML_EDIT_AANBOD;
    }

    @Override
    public void refreshScreen() {
        ObservableList<Aanbod> Aanbodlist = FXCollections.observableArrayList(DataSource.getInstance().queryAanbod());
        aanbodTable.itemsProperty().unbind();
        aanbodTable.setItems(Aanbodlist);
    }

    class GetAllAanbiedingenTask extends Task<ObservableList<Aanbod>> {

        @Override
        public ObservableList<Aanbod> call() {

            ObservableList<Aanbod> aanbodlist = FXCollections
                    .observableArrayList(DataSource.getInstance().queryAanbod());
            return aanbodlist;

        }
    }

}

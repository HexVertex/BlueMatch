package unofficial.bluematchgroup.bluematch;

import unofficial.bluematchgroup.bluematch.config.Constants;
import unofficial.bluematchgroup.bluematch.config.Resource;
import unofficial.bluematchgroup.bluematch.controllers.Controller;
import unofficial.bluematchgroup.bluematch.controllers.IController;

import java.io.IOException;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.util.Pair;

public class WindowManager {

    private static WindowManager instance;
    private Stack<Pair<IController, Scene>> controllerStack;
    private Stage window;

    public WindowManager() {
        controllerStack = new Stack<Pair<IController, Scene>>();
    }

    public void returnToMain() {
        while(!(controllerStack.peek().getKey() instanceof Controller)) {
            controllerStack.pop();
        } 
        Pair<IController, Scene> controlPair = controllerStack.peek();
        Controller controller = (Controller) controlPair.getKey();
        window.setScene(controlPair.getValue());
        window.show();
        controller.refreshScreen();
    }

    public void changeScene(Resource resource) throws IOException {
        View view = new View(resource);
        IController controller = view.getController();
        Scene scene = new Scene(view.getNode());
        controllerStack.push(new Pair<IController,Scene>(controller, scene));

        window.setScene((scene));
        window.show();
        controller.updateView();
    }

    public <T extends IController> Pair<T, Dialog<ButtonType>> buildDialog(Resource resource, ButtonType[] buttons) throws IOException {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        View view = new View(resource);
        dialog.getDialogPane().setContent(view.getNode());
        dialog.getDialogPane().getButtonTypes().addAll(buttons);
        return new Pair<T, Dialog<ButtonType>>(view.getController(), dialog);
    }

    public View initWindow(Stage stage) throws IOException {
        window = stage;
        View view = new View(Resource.FXML_MAIN);
        Controller controller = view.getController();
        Scene scene = new Scene(view.getNode(), Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
        controllerStack.push(new Pair<IController,Scene>(controller, scene));
        controller.listOverviewRecord();

        window.setTitle(Constants.TITLE);
        window.setScene(scene);
        window.show();

        return view;
    }

    public static WindowManager getInstance() {
        if (instance == null) {
            synchronized (WindowManager.class) {
                if (instance == null) {
                    instance = new WindowManager();
                }
            }
        }
        return instance;
    }

    public class View {

        private Parent node;
        private IController controller;

        public View(Resource resource) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource.toString()));
            node = loader.load();
            controller = loader.getController();
        }

        @SuppressWarnings("unchecked")
        public <T extends IController> T getController() throws ClassCastException {
            try {
                return (T) controller;
            } catch (ClassCastException e) {
                throw e;
            }
        }

        public Parent getNode() {
            return node;
        }

    }

}
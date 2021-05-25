package unofficial.bluematchgroup.bluematch.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import unofficial.bluematchgroup.bluematch.config.Config;

public class FormattedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    public FormattedTableCellFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableCell<S, T> call(TableColumn<S, T> p) {
        TableCell<S, T> cell = new TableCell<S, T>() {
            @Override
            protected void updateItem(Object item, boolean empty) {

                // CSS Styles
                // System.out.println("in tablecellfactory");
                String formatBiggerSize = "biggerSize";
                String formatevenBiggerSize = "evenbiggerSize";
                String defaultTableStyle = "table-cell";
                String cssStyle = "table-cell";

                // Remove all previously assigned CSS styles from the cell.
                getStyleClass().remove(formatevenBiggerSize);
                getStyleClass().remove(formatBiggerSize);
                getStyleClass().remove(defaultTableStyle);

                super.updateItem((T) item, empty);

                if (Config.getInstance().windowWidth > 1600) {
                    cssStyle = formatevenBiggerSize;
                } else {
                    if (Config.getInstance().windowWidth > 1050) {
                        cssStyle = formatBiggerSize;
                    } else {
                        cssStyle = defaultTableStyle;
                    }
                }

                getStyleClass().add(cssStyle);
                if (item != null) {
                    // System.out.println(cssStyle);
                    setText(item.toString());
                } else {
                    setText("");
                }
            }
        };
        return cell;
    }
}

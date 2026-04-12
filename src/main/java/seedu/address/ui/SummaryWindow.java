package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for the summary window.
 */
public class SummaryWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(SummaryWindow.class);
    private static final String FXML = "SummaryWindow.fxml";
    private static final String COLOR_PENDING = "#FFA500";
    private static final String COLOR_OFFERED = "#4CAF50";
    private static final String COLOR_REJECTED = "#F44336";
    private static final String COLOR_ARCHIVED = "#9E9E9E";
    private static final String COLOR_DEFAULT = "white";
    private static final int SUMMARY_WINDOW_WIDTH = 400;

    @FXML
    private TextFlow summaryMessage;

    /**
     * Creates a new SummaryWindow.
     *
     * @param root Stage to use as the root of the SummaryWindow.
     */
    public SummaryWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new SummaryWindow.
     */
    public SummaryWindow() {
        this(new Stage());
    }

    /**
     * Updates the content displayed in the summary window.
     */
    public void setContent(String content) {
        List<Text> nodes = new ArrayList<>();
        String[] lines = content.split("\n", -1);

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (i == 0) {
                Text title = new Text(line + "\n");
                title.setFont(Font.font("Segoe UI Semibold", FontWeight.BOLD, 22));
                title.setStyle("-fx-fill: white;");
                nodes.add(title);
                continue;
            }

            if (line.trim().isEmpty()) {
                Text sep = new Text("─────────────────────────\n");
                sep.setStyle("-fx-fill: #444444; -fx-font-size: 11px;");
                nodes.add(sep);
                continue;
            }

            if (line.contains(": ")) {
                String[] parts = line.split(": ", 2);
                String label = parts[0];
                String value = parts[1];

                Text labelText = new Text(label + ":  ");
                labelText.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 13px;");

                Text valueText = new Text(value + "\n");
                valueText.setStyle("-fx-fill: " + getValueColor(label) + "; -fx-font-size: 13px;");

                nodes.add(labelText);
                nodes.add(valueText);
            } else {
                Text plain = new Text(line + "\n");
                nodes.add(plain);
            }
        }

        summaryMessage.getChildren().setAll(nodes);
    }

    private String getValueColor(String label) {
        switch (label.trim()) {
        case "Pending":
            return COLOR_PENDING;
        case "Offered":
        case "Success Rate":
            return COLOR_OFFERED;
        case "Rejected":
            return COLOR_REJECTED;
        case "Archived":
            return COLOR_ARCHIVED;
        default:
            return COLOR_DEFAULT;
        }
    }

    /**
     * Shows the summary window.
     */
    public void show() {
        logger.fine("Showing summary window.");
        Stage stage = getRoot();

        stage.setWidth(SUMMARY_WINDOW_WIDTH);

        if (!stage.isShowing()) {
            stage.show();
        }

        stage.setIconified(false);
        stage.toFront();
        stage.requestFocus();
    }

    /**
     * Returns true if the summary window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the summary window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the summary window.
     */
    public void focus() {
        getRoot().requestFocus();
    }
}

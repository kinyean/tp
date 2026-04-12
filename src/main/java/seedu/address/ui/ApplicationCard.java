package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.application.Application;

/**
 * An UI component that displays information of a {@code Application}.
 */
public class ApplicationCard extends UiPart<Region> {

    private static final String FXML = "ApplicationListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Application application;

    @FXML
    private HBox cardPane;
    @FXML
    private Label companyName;
    @FXML
    private Label id;
    @FXML
    private Label role;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label website;
    @FXML
    private Label date;
    @FXML
    private Label status;
    @FXML
    private FlowPane tags;
    @FXML
    private Label archivedLabel;

    /**
     * Creates a {@code ApplicationCode} with the given {@code Application} and index to display.
     */
    public ApplicationCard(Application application, int displayedIndex) {
        super(FXML);
        this.application = application;
        id.setText(displayedIndex + ". ");
        companyName.setText(application.getCompanyName().fullCompanyName);
        role.setText(application.getRole().value);
        makeCopyable(address, application.getAddress() == null ? null : application.getAddress().value);
        makeCopyable(email, application.getEmail() == null ? null : application.getEmail().value);
        makeCopyable(website, application.getWebsite() == null ? null : application.getWebsite().websiteName);
        date.setText(application.getDate().value);
        status.setText(application.getStatus().toString());
        archivedLabel.setVisible(application.isArchived());
        archivedLabel.setManaged(application.isArchived());
        application.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
    private void makeCopyable(Label label, String text) {
        label.setText(text == null ? "" : text);
        if (text == null || text.isBlank()) {
            return;
        }
        label.setStyle("-fx-text-fill: #4FC3F7; -fx-underline: true;");
        label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: #81D4FA; -fx-cursor: hand;"));
        label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: #4FC3F7; -fx-underline: true"));
        label.setOnMouseClicked(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            Clipboard.getSystemClipboard().setContent(content);
        });
    }
}

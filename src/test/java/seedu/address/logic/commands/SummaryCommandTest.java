package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalApplications.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.ApplicationBuilder;

public class SummaryCommandTest {

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SummaryCommand().execute(null));
    }

    @Test
    public void execute_emptyModel_showsZeroCounts() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        String expectedText = "Application Summary\n\n"
                + "Total Applications: 0\n"
                + "Pending: 0\n"
                + "Offered: 0\n"
                + "Rejected: 0\n"
                + "Success Rate: N/A\n"
                + "\n"
                + "Archived: 0\n";
        CommandResult expectedCommandResult = new CommandResult(expectedText, UiAction.SHOW_SUMMARY);

        assertCommandSuccess(new SummaryCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_countsAreNonNegative() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        CommandResult result = new SummaryCommand().execute(model);
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Total Applications: "));
        for (String line : feedback.split("\n")) {
            if (line.contains(": ")) {
                String value = line.split(": ")[1].trim();
                try {
                    assertTrue(Long.parseLong(value) >= 0, "Count should be non-negative: " + line);
                } catch (NumberFormatException e) {
                    // Non-numeric values like "N/A" or "50.0%" are acceptable
                }
            }
        }
    }

    @Test
    public void execute_typicalModel_showsCorrectCounts() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        long archived = model.getAddressBook().getApplicationList().stream()
                .filter(a -> a.getTags().contains(Model.ARCHIVED_TAG))
                .count();
        long total = model.getAddressBook().getApplicationList().size() - archived;
        long pending = model.getAddressBook().getApplicationList().stream()
                .filter(a -> !a.getTags().contains(Model.ARCHIVED_TAG))
                .filter(a -> a.getStatus().toString().equalsIgnoreCase("Pending"))
                .count();
        long offered = model.getAddressBook().getApplicationList().stream()
                .filter(a -> !a.getTags().contains(Model.ARCHIVED_TAG))
                .filter(a -> a.getStatus().toString().equalsIgnoreCase("Offered"))
                .count();
        long rejected = model.getAddressBook().getApplicationList().stream()
                .filter(a -> !a.getTags().contains(Model.ARCHIVED_TAG))
                .filter(a -> a.getStatus().toString().equalsIgnoreCase("Rejected"))
                .count();

        String successRate = (offered + rejected) > 0
                ? String.format("%.1f%%", (offered * 100.0) / (offered + rejected))
                : "N/A";

        String expectedText = "Application Summary\n\n"
                + "Total Applications: " + total + "\n"
                + "Pending: " + pending + "\n"
                + "Offered: " + offered + "\n"
                + "Rejected: " + rejected + "\n"
                + "Success Rate: " + successRate + "\n"
                + "\n"
                + "Archived: " + archived + "\n";
        CommandResult expectedCommandResult = new CommandResult(expectedText, UiAction.SHOW_SUMMARY);

        assertCommandSuccess(new SummaryCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_withArchivedApplication_excludesFromCounts() {
        AddressBook ab = new AddressBook();
        ab.addApplication(new ApplicationBuilder().withName("Active Co").withStatus("Offered").build());
        ab.addApplication(new ApplicationBuilder().withName("Archived Co").withStatus("Rejected")
                .withTags("archived").build());

        Model model = new ModelManager(ab, new UserPrefs());
        Model expectedModel = new ModelManager(ab, new UserPrefs());

        String expectedText = "Application Summary\n\n"
                + "Total Applications: 1\n"
                + "Pending: 0\n"
                + "Offered: 1\n"
                + "Rejected: 0\n"
                + "Success Rate: 100.0%\n"
                + "\n"
                + "Archived: 1\n";
        CommandResult expectedCommandResult = new CommandResult(expectedText, UiAction.SHOW_SUMMARY);

        assertCommandSuccess(new SummaryCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_withOfferedAndRejected_showsSuccessRate() {
        AddressBook ab = new AddressBook();
        ab.addApplication(new ApplicationBuilder().withName("Company A").withStatus("Offered").build());
        ab.addApplication(new ApplicationBuilder().withName("Company B").withStatus("Rejected").build());
        ab.addApplication(new ApplicationBuilder().withName("Company C").withStatus("Rejected").build());

        Model model = new ModelManager(ab, new UserPrefs());
        Model expectedModel = new ModelManager(ab, new UserPrefs());

        String expectedText = "Application Summary\n\n"
                + "Total Applications: 3\n"
                + "Pending: 0\n"
                + "Offered: 1\n"
                + "Rejected: 2\n"
                + "Success Rate: 33.3%\n"
                + "\n"
                + "Archived: 0\n";
        CommandResult expectedCommandResult = new CommandResult(expectedText, UiAction.SHOW_SUMMARY);

        assertCommandSuccess(new SummaryCommand(), model, expectedCommandResult, expectedModel);
    }
}

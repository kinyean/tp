package systemtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;


/**
 * Simple integration-style tests for the delete command using LogicManager and
 * temporary JSON storage. This provides an automated way to exercise the
 * delete command end-to-end (parsing, execution, storage save) without
 * requiring a full UI test harness.
 */
public class DeleteCommandSystemTest {

    @TempDir
    public Path temporaryFolder;

    private Model model;
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_deleteFirst_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String command = DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased();

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        CommandResult result = logic.execute(command);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_caseInsensitiveCommand_fail() throws Exception {
        String command = "Delete" + " " + INDEX_FIRST_PERSON.getOneBased();
        String expectedMessage = Messages.MESSAGE_UNKNOWN_COMMAND;
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        try {
            logic.execute(command);
            throw new AssertionError("Expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
            assertEquals(expectedModel, model);
        }
    }

    @Test
    public void execute_invalidIndex_fail() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        String command = DeleteCommand.COMMAND_WORD + " " + outOfBoundIndex.getOneBased();
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        try {
            logic.execute(command);
            throw new AssertionError("Expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertEquals(expectedModel, model);
        } catch (ParseException pe) {
            throw new AssertionError("Unexpected ParseException: " + pe.getMessage(), pe);
        }
    }
}

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.tag.Tag;

/**
 * Unarchives an application identified using its displayed index from the application list.
 */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unarchives the application identified by the index number used in the displayed application list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNARCHIVE_APPLICATION_SUCCESS = "Unarchived Application: %1$s";
    public static final String MESSAGE_APPLICATION_NOT_ARCHIVED = "This application is not archived.";

    private final Index targetIndex;

    public UnarchiveCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Application> lastShownList = model.getFilteredApplicationList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPLICATION_DISPLAYED_INDEX);
        }

        Application applicationToUnarchive = lastShownList.get(targetIndex.getZeroBased());

        if (!applicationToUnarchive.getTags().contains(Model.ARCHIVED_TAG)) {
            return new CommandResult(MESSAGE_APPLICATION_NOT_ARCHIVED);
        }

        Set<Tag> unarchivedTags = new HashSet<>(applicationToUnarchive.getTags());
        unarchivedTags.remove(Model.ARCHIVED_TAG);

        Application unarchivedApplication = new Application(
                applicationToUnarchive.getCompanyName(),
                applicationToUnarchive.getRole(),
                applicationToUnarchive.getEmail(),
                applicationToUnarchive.getWebsite(),
                applicationToUnarchive.getAddress(),
                applicationToUnarchive.getDate(),
                applicationToUnarchive.getStatus(),
                unarchivedTags,
                applicationToUnarchive.getNotes()
        );

        model.setApplication(applicationToUnarchive, unarchivedApplication);
        model.updateFilteredApplicationList(Model.PREDICATE_SHOW_UNARCHIVED_APPLICATIONS);

        return new CommandResult(String.format(MESSAGE_UNARCHIVE_APPLICATION_SUCCESS,
                Messages.format(unarchivedApplication)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnarchiveCommand)) {
            return false;
        }

        UnarchiveCommand otherUnarchiveCommand = (UnarchiveCommand) other;
        return targetIndex.equals(otherUnarchiveCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}

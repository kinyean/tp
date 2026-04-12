package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.application.Application;

/**
 * Finds and lists all applications in address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds applications using the given fields (prefix + keyword).\n"
            + "Each prefix(e.g. n/) specifies a field, and keyword is used for filtering "
            + "(case-insensitive, substring partial match).\n"
            + "At least one field must be provided.\n"
            + "For optional fields (email, website, address),"
            + " using an empty keyword (e.g. find e/) matches applications with no value for that field.\n"
            + "Format: find FIELD [FIELD]...\n"
            + "Fields: n/NAME, r/ROLE, e/EMAIL, w/WEBSITE, a/ADDRESS, d/DATE, s/STATUS, t/TAG\n"
            + "Example: " + COMMAND_WORD + " n/Google r/Backend Developer s/Pending";

    private final Predicate<Application> predicate;

    public FindCommand(Predicate<Application> predicate) {
        this.predicate = predicate;
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredApplicationList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_APPLICATIONS_LISTED_OVERVIEW,
                        model.getFilteredApplicationList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}

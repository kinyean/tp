package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.application.Address;
import seedu.address.model.application.Application;
import seedu.address.model.application.CompanyName;
import seedu.address.model.application.Date;
import seedu.address.model.application.Email;
import seedu.address.model.application.Role;
import seedu.address.model.application.Status;
import seedu.address.model.application.Website;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Application}.
 */
class JsonAdaptedApplication {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Application's %s field is missing!";

    private final String companyName;
    private final String role;
    private final String email;
    private final String website;
    private final String date;
    private final String address;
    private final String status;
    private final String notes;
    private final Boolean isArchived;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedApplication} with the given application details.
     */
    @JsonCreator
    public JsonAdaptedApplication(@JsonProperty("companyName") String companyName, @JsonProperty("role") String role,
                                  @JsonProperty("email") String email, @JsonProperty("website") String website,
                                  @JsonProperty("address") String address, @JsonProperty("date") String date,
                                  @JsonProperty("status") String status, @JsonProperty("notes") String notes,
                                  @JsonProperty("isArchived") Boolean isArchived,
                                  @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.companyName = companyName;
        this.role = role;
        this.email = email;
        this.website = website;
        this.address = address;
        this.date = date;
        this.status = status;
        this.notes = notes;
        this.isArchived = isArchived;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Application} into this class for Jackson use.
     */
    public JsonAdaptedApplication(Application source) {
        companyName = source.getCompanyName().fullCompanyName;
        role = source.getRole().value;
        email = source.getEmail() == null ? null : source.getEmail().value;
        website = source.getWebsite() == null ? null : source.getWebsite().websiteName;
        address = source.getAddress() == null ? null : source.getAddress().value;
        date = source.getDate().value;
        status = source.getStatus().toString();
        notes = source.getNotes();
        isArchived = source.isArchived();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted application object into the model's {@code Application} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted application.
     */
    public Application toModelType() throws IllegalValueException {
        final List<Tag> applicationTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            applicationTags.add(tag.toModelType());
        }

        final String modelNotes = notes == null ? "" : notes;
        final boolean modelIsArchived = isArchived != null && isArchived;
        final Set<Tag> modelTags = new HashSet<>(applicationTags);
        return new Application(
                parseCompanyName(), parseRole(), parseEmail(), parseWebsite(),
                parseAddress(), parseDate(), parseStatus(), modelTags, modelNotes, modelIsArchived
        );
    }

    /** Parses and validates the company name field. */
    private CompanyName parseCompanyName() throws IllegalValueException {
        if (companyName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CompanyName.class.getSimpleName()));
        }
        if (!CompanyName.isValidCompanyName(companyName)) {
            throw new IllegalValueException(CompanyName.MESSAGE_CONSTRAINTS);
        }
        return new CompanyName(companyName);
    }

    /** Parses and validates the role field. */
    private Role parseRole() throws IllegalValueException {
        if (role == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Role.class.getSimpleName()));
        }
        if (!Role.isValidRole(role)) {
            throw new IllegalValueException(Role.MESSAGE_CONSTRAINTS);
        }
        return new Role(role);
    }

    /** Parses and validates the optional email field. Returns null if not provided. */
    private Email parseEmail() throws IllegalValueException {
        if (email == null) {
            return null;
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(email);
    }

    /** Parses and validates the optional website field. Returns null if not provided. */
    private Website parseWebsite() throws IllegalValueException {
        if (website == null) {
            return null;
        }
        if (!Website.isValidWebsite(website)) {
            throw new IllegalValueException(Website.MESSAGE_CONSTRAINTS);
        }
        return new Website(website);
    }

    /** Parses and validates the optional address field. Returns null if not provided. */
    private Address parseAddress() throws IllegalValueException {
        if (address == null) {
            return null;
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(address);
    }

    /** Parses and validates the date field. */
    private Date parseDate() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }
        return new Date(date);
    }

    /** Parses and validates the status field. */
    private Status parseStatus() throws IllegalValueException {
        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName()));
        }
        if (!Status.isValidStatus(status)) {
            throw new IllegalValueException(Status.MESSAGE_CONSTRAINTS);
        }
        return new Status(status);
    }

}

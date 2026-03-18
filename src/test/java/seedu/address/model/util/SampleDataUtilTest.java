package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.application.Application;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsCorrectNumberOfApplications() {
        Application[] applications = SampleDataUtil.getSampleApplications();
        assertNotNull(applications);
        assertEquals(6, applications.length);
    }

    @Test
    public void getSamplePersons_applicationsHaveValidDates() {
        Application[] applications = SampleDataUtil.getSampleApplications();
        for (Application application : applications) {
            assertNotNull(application.getDate());
        }
    }

    @Test
    public void getSampleAddressBook_containsSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(addressBook);
        assertEquals(6, addressBook.getApplicationList().size());
    }
}

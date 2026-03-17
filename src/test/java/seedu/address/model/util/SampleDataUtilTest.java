package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsCorrectNumberOfPersons() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertNotNull(persons);
        assertEquals(6, persons.length);
    }

    @Test
    public void getSamplePersons_personsHaveValidDates() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        for (Person person : persons) {
            assertNotNull(person.getDate());
        }
    }

    @Test
    public void getSampleAddressBook_containsSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(addressBook);
        assertEquals(6, addressBook.getPersonList().size());
    }
}

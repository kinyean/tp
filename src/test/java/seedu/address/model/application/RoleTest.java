package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        String invalidRole = "";
        assertThrows(IllegalArgumentException.class, () -> new Role(invalidRole));
    }

    @Test
    public void isValidRole() {
        // null role
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));

        // invalid roles
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only
        assertFalse(Role.isValidRole(" Engineer")); // leading space

        // valid roles
        assertTrue(Role.isValidRole("E")); // single character
        assertTrue(Role.isValidRole("Engineer")); // alphabets
        assertTrue(Role.isValidRole("Engineer  ")); // trailing spaces
        assertTrue(Role.isValidRole("Software Engineer")); // spaces inside
        assertTrue(Role.isValidRole("Software    Engineer")); // multiple spaces inside
        assertTrue(Role.isValidRole("SoftwareEngineer1")); // alphanumeric
        assertTrue(Role.isValidRole("Backend Developer2")); // alphanumeric with space
        assertTrue(Role.isValidRole("Backend     Developer2")); // alphanumeric with multiple spaces
        assertTrue(Role.isValidRole("@Engineer")); // special characters at the beginning
        assertTrue(Role.isValidRole("Dev!Ops")); // special characters inside
        assertTrue(Role.isValidRole("Engineer#")); // special characters at the end
    }

    @Test
    public void constructor_normalization() {
        Role role = new Role("  Software     Engineer  ");
        assertEquals("Software Engineer", role.value);
    }

    @Test
    public void isSameRole() {
        // same object
        Role role = new Role("Software Engineer");
        assertTrue(role.isSameRole(role));

        // null
        assertFalse(role.isSameRole(null));

        // same role, different case
        assertTrue(role.isSameRole(new Role("software engineer")));

        // same role, multiple spaces
        assertTrue(role.isSameRole(new Role("Software     Engineer")));

        // same role, leading/trailing spaces
        assertTrue(role.isSameRole(new Role("  Software Engineer  ")));

        // same role, different case and multiple spaces
        assertTrue(role.isSameRole(new Role("  Software    engineer  ")));

        // different role
        assertFalse(role.isSameRole(new Role("Backend Developer")));
    }

    @Test
    public void equals() {
        Role role = new Role("Algorithm Engineer");

        // same values -> returns true
        assertTrue(role.equals(new Role("Algorithm Engineer")));

        // same object -> returns true
        assertTrue(role.equals(role));

        // null -> returns false
        assertFalse(role.equals(null));

        // different types -> returns false
        assertFalse(role.equals(5.0f));

        // different values -> returns false
        assertFalse(role.equals(new Role("Backend Developer")));
    }

    @Test
    public void equalsVsIsSameRole_caseDifference() {
        Role role1 = new Role("Software Engineer");
        Role role2 = new Role("software engineer");

        assertTrue(role1.isSameRole(role2)); // logical same
        assertTrue(role2.isSameRole(role1));
        assertFalse(role1.equals(role2)); // strict different
    }
}

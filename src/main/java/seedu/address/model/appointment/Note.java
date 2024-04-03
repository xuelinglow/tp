package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Note in the appointment
 * Guarantees: immutable; note can take any value
 */
public class Note {
    public static final int NOTE_CHARACTER_LIMIT = 70;

    public static final String MESSAGE_CONSTRAINTS =
            "Note should have less than " + NOTE_CHARACTER_LIMIT + " characters";

    public final String note;

    /**
     * Constructs a {@code Note}.
     *
     * @param note Any note.
     */
    public Note(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_CONSTRAINTS);
        this.note = note;
    }

    /**
     * Returns true if a given string is a valid appointment note.
     */
    public static boolean isValidNote(String test) {
        return test.trim().length() < NOTE_CHARACTER_LIMIT;
    }


    @Override
    public String toString() {
        return note;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Note)) {
            return false;
        }

        Note otherNote = (Note) other;
        return note.equals(otherNote.note);
    }

    @Override
    public int hashCode() {
        return note.hashCode();
    }

}

package seedu.address.testutil;

import seedu.address.commons.core.date.Date;
import seedu.address.logic.commands.EditApptCommand.EditApptDescriptor;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentType;
import seedu.address.model.appointment.Note;
import seedu.address.model.appointment.Time;

/**
 * A utility class to help with building EditApptDescriptor objects.
 */
public class EditApptDescriptorBuilder {

    private EditApptDescriptor descriptor;

    public EditApptDescriptorBuilder() {
        descriptor = new EditApptDescriptor();
    }

    public EditApptDescriptorBuilder(EditApptDescriptor descriptor) {
        this.descriptor = new EditApptDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditApptDescriptor} with fields containing {@code appointment}'s details
     */
    public EditApptDescriptorBuilder(Appointment appointment) {
        descriptor = new EditApptDescriptor();
        descriptor.setDate(appointment.getDate());
        descriptor.setStartTime(appointment.getStartTime());
        descriptor.setEndTime(appointment.getEndTime());
        descriptor.setAppointmentType(appointment.getAppointmentType());
        descriptor.setNote(appointment.getNote());
    }

    /**
     * Sets the {@code Date} of the {@code EditApptDescriptor} that we are building.
     */
    public EditApptDescriptorBuilder withDate(String date) {
        descriptor.setDate(new Date(date));
        return this;
    }

    /**
     * Sets the {@code StartTime} of the {@code EditApptDescriptor} that we are building.
     */
    public EditApptDescriptorBuilder withStartTime(String startTime) {
        descriptor.setStartTime(new Time(startTime));
        return this;
    }

    /**
     * Sets the {@code EndTime} of the {@code EditApptDescriptor} that we are building.
     */
    public EditApptDescriptorBuilder withEndTime(String endTime) {
        descriptor.setEndTime(new Time(endTime));
        return this;
    }

    /**
     * Sets the {@code AppointmentType} of the {@code EditApptDescriptor} that we are building.
     */
    public EditApptDescriptorBuilder withAppointmentType(String appointmentType) {
        descriptor.setAppointmentType(new AppointmentType(appointmentType));
        return this;
    }

    /**
     * Sets the {@code Note} of the {@code EditApptDescriptor} that we are building.
     */
    public EditApptDescriptorBuilder withNote(String note) {
        descriptor.setNote(new Note(note));
        return this;
    }
    public EditApptDescriptor build() {
        return descriptor;
    }
}

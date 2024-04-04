package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;

public class NoSelectionModelTest {

    @Test
    public void testGetSelectedIndices() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        ObservableList<Integer> selectedIndices = selectionModel.getSelectedIndices();
        assertTrue(selectedIndices.isEmpty());
    }


    @Test
    public void testGetSelectedItems() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        ObservableList<String> selectedItems = selectionModel.getSelectedItems();
        assertTrue(selectedItems.isEmpty());
    }

    @Test
    public void testSelectIndices() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.selectIndices(0); // Should not throw an exception
    }

    @Test
    public void testSelectAll() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.selectAll(); // Should not throw an exception
    }

    @Test
    public void testSelectFirst() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.selectFirst(); // Should not throw an exception
    }

    @Test
    public void testSelectLast() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.selectLast(); // Should not throw an exception
    }

    @Test
    public void testClearAndSelect() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.clearAndSelect(0); // Should not throw an exception
    }

    @Test
    public void testSelect() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.select(0); // Should not throw an exception
        selectionModel.select("test"); // Should not throw an exception
    }

    @Test
    public void testClearSelection() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.clearSelection(0); // Should not throw an exception
        selectionModel.clearSelection(); // Should not throw an exception
    }

    @Test
    public void testisSelected() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        assertFalse(selectionModel.isSelected(0));
    }

    @Test
    public void testisEmpty() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        assertTrue(selectionModel.isEmpty());
    }

    @Test
    public void testSelectPrevious() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.selectPrevious(); // Should not throw an exception
    }

    @Test
    public void testSelectNext() {
        NoSelectionModel<String> selectionModel = new NoSelectionModel<>();
        selectionModel.selectNext(); // Should not throw an exception
    }

}

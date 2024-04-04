package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_NRIC = new Prefix("i/");
    public static final Prefix PREFIX_DOB = new Prefix("b/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_DATE = new Prefix("d/");
    public static final Prefix PREFIX_START_TIME = new Prefix("from/");
    public static final Prefix PREFIX_END_TIME = new Prefix("to/");
    public static final Prefix PREFIX_NOTE = new Prefix("note/");

    public static final Prefix PREFIX_NEW_TAG = new Prefix("newt/");
    public static final Prefix PREFIX_NEW_DATE = new Prefix("newd/");
    public static final Prefix PREFIX_NEW_START_TIME = new Prefix("newfrom/");
    public static final Prefix PREFIX_NEW_END_TIME = new Prefix("newto/");
    public static final Prefix PREFIX_NEW_NOTE = new Prefix("newnote/");
    public static final Prefix PREFIX_NEW_NAME = new Prefix("newn/");
    public static final Prefix PREFIX_NEW_DOB = new Prefix("newb/");
    public static final Prefix PREFIX_NEW_PHONE = new Prefix("newp/");
    public static final Prefix PREFIX_NEW_EMAIL = new Prefix("newe/");
    public static final Prefix PREFIX_NEW_ADDRESS = new Prefix("newa/");

    public static List<Prefix> getAllPrefixes() {
        List<Prefix> allPrefixes = new ArrayList<>();
        allPrefixes.add(PREFIX_NAME);
        allPrefixes.add(PREFIX_NRIC);
        allPrefixes.add(PREFIX_DOB);
        allPrefixes.add(PREFIX_PHONE);
        allPrefixes.add(PREFIX_EMAIL);
        allPrefixes.add(PREFIX_ADDRESS);
        allPrefixes.add(PREFIX_TAG);
        allPrefixes.add(PREFIX_DATE);
        allPrefixes.add(PREFIX_START_TIME);
        allPrefixes.add(PREFIX_END_TIME);
        allPrefixes.add(PREFIX_NOTE);
        allPrefixes.add(PREFIX_NEW_TAG);
        allPrefixes.add(PREFIX_NEW_DATE);
        allPrefixes.add(PREFIX_NEW_START_TIME);
        allPrefixes.add(PREFIX_NEW_END_TIME);
        allPrefixes.add(PREFIX_NEW_NOTE);
        allPrefixes.add(PREFIX_NEW_NAME);
        allPrefixes.add(PREFIX_NEW_DOB);
        allPrefixes.add(PREFIX_NEW_PHONE);
        allPrefixes.add(PREFIX_NEW_EMAIL);
        allPrefixes.add(PREFIX_NEW_ADDRESS);
        return allPrefixes;
    }

    public static List<Prefix> getNewPrefixes() {
        List<Prefix> newPrefixes = new ArrayList<>();
        newPrefixes.add(PREFIX_NEW_TAG);
        newPrefixes.add(PREFIX_NEW_DATE);
        newPrefixes.add(PREFIX_NEW_START_TIME);
        newPrefixes.add(PREFIX_NEW_END_TIME);
        newPrefixes.add(PREFIX_NEW_NOTE);
        newPrefixes.add(PREFIX_NEW_NAME);
        newPrefixes.add(PREFIX_NEW_DOB);
        newPrefixes.add(PREFIX_NEW_PHONE);
        newPrefixes.add(PREFIX_NEW_EMAIL);
        newPrefixes.add(PREFIX_NEW_ADDRESS);
        return newPrefixes;
    }

}

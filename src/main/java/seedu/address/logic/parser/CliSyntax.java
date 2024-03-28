package seedu.address.logic.parser;

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
}

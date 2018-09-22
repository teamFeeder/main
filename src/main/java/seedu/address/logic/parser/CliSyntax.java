package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_MEDICAL_RECORD = new Prefix("mr/");
    public static final Prefix PREFIX_DATE = new Prefix("/d");
    public static final Prefix PREFIX_MEDICAL_DEPARTMENT = new Prefix("/md");
    public static final Prefix PREFIX_NRIC = new Prefix("/ic");
    public static final Prefix PREFIX_ROLE = new Prefix("/r");
}

/* (C)2023 */
package com.recipecart.utils;

import org.apache.commons.cli.*;

/**
 * This class is meant for formatting command-line arguments passed in, for easy usage by other
 * classes
 */
public class CommandLineArguments {
    private static final String DEFAULT_FILENAME = "src/main/resources/entities.ser",
            DEFAULT_PORT = "4567",
            DEFAULT_UPDATES_PER_AUTOSAVE = "1",
            FILENAME_OPTION = "f",
            PORT_OPTION = "p",
            UPDATES_PER_AUTOSAVE_OPTION = "u",
            DISABLE_FINAL_SAVE_OPTION = "d",
            MOCK_DATA_OPTION = "m",
            HELP_OPTION = "h";
    private static final int MIN_PORT = 1024, MAX_PORT = 65535, NO_AUTOSAVE_VALUE = 0;

    private final Options options;
    private final Integer port, updatesPerAutosave;
    private final Boolean autosave, disableFinalSave, mockData, help, valid;
    private final String filename;

    /**
     * Formats and initializes the command-line arguments for the RecipeCart backend
     *
     * @param args the arguments passed into the RecipeCart backend via command-line
     */
    public CommandLineArguments(String[] args) {
        this.options = initCommandLineOptions();
        CommandLine commandLine = parseArgs(this.options, args);
        if (commandLine == null) {
            this.filename = null;
            this.port = null;
            this.updatesPerAutosave = null;
            this.autosave = null;
            this.disableFinalSave = null;
            this.mockData = null;
            this.help = null;
            this.valid = false;
            return;
        }

        this.filename = commandLine.getOptionValue(FILENAME_OPTION, DEFAULT_FILENAME);

        String portStr = commandLine.getOptionValue(PORT_OPTION, DEFAULT_PORT);
        this.port = Utils.isNumber(portStr) ? Integer.parseInt(portStr) : null;

        String updatesStr =
                commandLine.getOptionValue(
                        UPDATES_PER_AUTOSAVE_OPTION, DEFAULT_UPDATES_PER_AUTOSAVE);
        this.updatesPerAutosave = Utils.isNumber(updatesStr) ? Integer.parseInt(updatesStr) : null;
        this.autosave =
                getUpdatesPerAutosave() != null && getUpdatesPerAutosave() != NO_AUTOSAVE_VALUE;

        this.disableFinalSave = commandLine.hasOption(DISABLE_FINAL_SAVE_OPTION);

        this.mockData = commandLine.hasOption(MOCK_DATA_OPTION);

        this.help = commandLine.hasOption(HELP_OPTION);

        this.valid = checkValidity();
    }

    private Options getOptions() {
        return options;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getUpdatesPerAutosave() {
        return updatesPerAutosave;
    }

    public boolean isDisableFinalSave() {
        return disableFinalSave;
    }

    public boolean isMockData() {
        return mockData;
    }

    public boolean isAutosave() {
        return autosave;
    }

    public boolean isHelp() {
        return help;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isValid() {
        return valid;
    }

    /** Prints the help message for the RecipeCart backend command line arguments. */
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("recipecart", getOptions());
    }

    private boolean checkValidity() {
        return getPort() != null
                && getPort() >= MIN_PORT
                && getPort() <= MAX_PORT
                && getUpdatesPerAutosave() != null;
    }

    private static CommandLine parseArgs(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("An error has occurred when parsing the arguments");
            return null;
        }
    }

    private static Options initCommandLineOptions() {
        Options options = new Options();

        Option filename =
                Option.builder(FILENAME_OPTION)
                        .longOpt("filename")
                        .argName("file")
                        .hasArg()
                        .desc(
                                "The location/name of the file the entity data are saved in."
                                        + " Defaults to "
                                        + DEFAULT_FILENAME
                                        + ". If the file doesn't exist, the server starts with no"
                                        + " entity data, and the file is created upon saving.")
                        .build();
        options.addOption(filename);

        Option port =
                Option.builder(PORT_OPTION)
                        .longOpt("port")
                        .argName("portnum")
                        .hasArg()
                        .desc(
                                "The port for this server to listen in on. Defaults to "
                                        + DEFAULT_PORT
                                        + ". Must be between "
                                        + MIN_PORT
                                        + " and "
                                        + MAX_PORT
                                        + " inclusive.")
                        .build();
        options.addOption(port);

        Option disableFinalSave =
                Option.builder(DISABLE_FINAL_SAVE_OPTION)
                        .longOpt("disable-final-save")
                        .hasArg(false)
                        .desc("Disable the final save to file when quitting this program")
                        .build();
        options.addOption(disableFinalSave);

        Option updatesPerAutosave =
                Option.builder(UPDATES_PER_AUTOSAVE_OPTION)
                        .longOpt("updates-per-autosave")
                        .argName("num")
                        .hasArg()
                        .desc(
                                "Entity data is autosaved to the file every [this argument] number"
                                        + " of times. Must be a integer. Defaults to "
                                        + DEFAULT_UPDATES_PER_AUTOSAVE
                                        + ". Autosaving is disabled if set to 0.")
                        .build();
        options.addOption(updatesPerAutosave);

        Option mock =
                Option.builder(MOCK_DATA_OPTION)
                        .longOpt("mock-data")
                        .hasArg(false)
                        .desc(
                                "Pre-populate the entity data with some mock entity data."
                                        + " Pre-population can cause autosaving")
                        .build();
        options.addOption(mock);

        Option help =
                Option.builder(HELP_OPTION)
                        .longOpt("help")
                        .hasArg(false)
                        .desc("Print this message and exit.")
                        .build();
        options.addOption(help);

        return options;
    }
}

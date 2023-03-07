/* (C)2023 */
package com.recipecart;

import static spark.Spark.awaitStop;
import static spark.Spark.stop;

import com.recipecart.database.FileEntitySaveAndLoader;
import com.recipecart.execution.EntityCommander;
import com.recipecart.requests.HttpRequestHandler;
import com.recipecart.requests.JwtValidator;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import com.recipecart.storage.EntityStorage;
import com.recipecart.utils.CommandLineArguments;
import com.recipecart.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This is the class that runs the backend server. The general workflow for the backend server is:
 *
 * <ul>
 *   <li>Backend gets started up, and listens for requests from the front-end.
 *   <li>HttpRequestHandler takes that request and translates it into an EntityCommand pass into the
 *       business logic layer (through EntityCommander).
 *   <li>Command is executed in business logic layer, performing the use case outlined by the
 *       original request.
 *   <li>During the execution of this use case, the command may save and load entities from the data
 *       access layer.
 *   <li>After the command has finished executing, HttpRequestHandler looks at the output and sends
 *       the appropriate HTTP response back to the front-end.
 * </ul>
 */
public class Main {
    public static final String SERVER_STOP_STRING = "quit";

    /**
     * Starts up the backend server. Initializes the EntityStorage (which currently uses the
     * EntitySaver/EntityLoader implemented by FileEntitySaverAndLoader, which also means that
     * entities are loaded through a file), gives it to an EntityCommander that is initialized,
     * gives that to a HttpRequestHandler that is initialized, and has the HttpRequestHandler listen
     * for requests from the front-end.
     *
     * @param args raw command-line arguments passed into this program
     * @throws IOException if an error occurs when loading entities from file
     * @throws ClassNotFoundException if an error occurs with finding a class when deserializing the
     *     file entities are stored in
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        CommandLineArguments commandArgs = new CommandLineArguments(args);
        checkArgumentsValidity(commandArgs);
        checkHelp(commandArgs);

        FileEntitySaveAndLoader saveAndLoader =
                initSaveAndLoader(
                        commandArgs.getFilename(),
                        commandArgs.isAutosave(),
                        commandArgs.getUpdatesPerAutosave(),
                        commandArgs.isMockData());
        initHandler(saveAndLoader, saveAndLoader, commandArgs.getPort());
        listenForStopString(
                saveAndLoader, !commandArgs.isDisableFinalSave(), commandArgs.getFilename());
    }

    private static void checkArgumentsValidity(CommandLineArguments commandArgs) {
        if (!commandArgs.isValid()) {
            commandArgs.printHelp();
            System.exit(1);
        }
    }

    private static void checkHelp(CommandLineArguments commandArgs) {
        if (commandArgs.isHelp()) {
            commandArgs.printHelp();
            System.exit(0);
        }
    }

    private static FileEntitySaveAndLoader initSaveAndLoader(
            String filename, boolean autosave, int updatesPerAutosave, boolean mockData)
            throws IOException, ClassNotFoundException {
        if (autosave && updatesPerAutosave <= 0) {
            throw new IllegalArgumentException(
                    "Autosaving enabled, but invalid updatesPerAutosave argument");
        }

        FileEntitySaveAndLoader saveAndLoader;
        if (autosave) {
            saveAndLoader = new FileEntitySaveAndLoader(filename, updatesPerAutosave);
        } else {
            saveAndLoader = new FileEntitySaveAndLoader();
        }

        if (new File(filename).exists()) {
            saveAndLoader.load(filename);
        }
        if (mockData) {
            Utils.putInMockData(saveAndLoader);
        }
        return saveAndLoader;
    }

    private static void initHandler(EntitySaver saver, EntityLoader loader, int port) {
        EntityStorage storage = new EntityStorage(saver, loader);
        EntityCommander commander = new EntityCommander(storage);
        JwtValidator validator = new JwtValidator();
        HttpRequestHandler requestHandler = new HttpRequestHandler(commander, validator, port);

        requestHandler.startHandler();
    }

    private static void listenForStopString(
            FileEntitySaveAndLoader saveAndLoader, boolean save, String filename)
            throws IOException {
        if (save && filename == null) {
            throw new IllegalArgumentException("Saving enabled, but filename is null");
        }

        System.out.println(
                "Server started! Enter in \""
                        + SERVER_STOP_STRING
                        + "\" (into stdin) to stop the server");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            if (sc.nextLine().equals(SERVER_STOP_STRING)) {
                stop();
                if (save) {
                    saveAndLoader.save(filename);
                }
                awaitStop();
                System.exit(0);
            }
        }
    }
}

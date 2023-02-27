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
import com.recipecart.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final int PORT = 4567, SAVER_SAVES_PER_FILE_SAVE = 1;
    public static final boolean PUT_MOCK_DATA_IF_FRESH = true, SAVE_TO_FILE = true;
    public static final String SERVER_STOP_STRING = "quit",
            FILENAME = "src/main/resources/entities.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileEntitySaveAndLoader saveAndLoader = initSaveAndLoader();
        initHandler(saveAndLoader, saveAndLoader);
        listenForStopString(saveAndLoader);
    }

    private static FileEntitySaveAndLoader initSaveAndLoader()
            throws IOException, ClassNotFoundException {
        FileEntitySaveAndLoader saveAndLoader;
        if (SAVE_TO_FILE) {
            saveAndLoader = new FileEntitySaveAndLoader(FILENAME, SAVER_SAVES_PER_FILE_SAVE);
        } else {
            saveAndLoader = new FileEntitySaveAndLoader();
        }

        if (new File(FILENAME).exists()) {
            saveAndLoader.load(FILENAME);
        } else if (PUT_MOCK_DATA_IF_FRESH) {
            Utils.putInMockData(saveAndLoader);
            if (SAVE_TO_FILE) {
                saveAndLoader.save(FILENAME);
            }
        }
        return saveAndLoader;
    }

    private static void initHandler(EntitySaver saver, EntityLoader loader) {
        EntityStorage storage = new EntityStorage(saver, loader);
        EntityCommander commander = new EntityCommander(storage);
        JwtValidator validator = new JwtValidator();
        HttpRequestHandler requestHandler = new HttpRequestHandler(commander, validator, PORT);

        requestHandler.startHandler();
    }

    private static void listenForStopString(FileEntitySaveAndLoader saveAndLoader)
            throws IOException {
        System.out.println(
                "Server started! Enter in \""
                        + SERVER_STOP_STRING
                        + "\" (into stdin) to stop the server");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            if (sc.nextLine().equals(SERVER_STOP_STRING)) {
                stop();
                if (SAVE_TO_FILE) {
                    saveAndLoader.save(FILENAME);
                }
                awaitStop();
                System.exit(0);
            }
        }
    }
}

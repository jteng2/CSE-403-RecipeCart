# Developer Manual

# How to obtain the source code
All source code for RecipeCart is located in this repository.

# Repository layout
Main Folders: front-end, back-end, database, and reports
- `front-end`: Front-end related files
- `src`: back-end related files
- `reports`: weekly updates on each collaborators' progress and goals

### More info on `src`
The file structure of `src` looks like this:
- `src`
    - `/main/java`: back-end source code
        - `/Main.java`: what initializes each layer of the backend architecture (presentation, business logic, data access), connects layers as appropriate, and starts listening for requests from the front-end.
        - `/com/recipecart`
            - `/database`: code that pertains to connecting to the Mongo database for reading/writing recipe/user/etc. data
            - `/entities`: the RecipeCart entities (Recipes, Users, Ingredients, Tags) that each house appropriate information
            - `/execution`: code pertaining to receiving, pre-processing, and executing commands for use cases
            - `/requests`: code pertaining to handling requests from the front-end
            - `/storage`: code that houses general interfaces/classes for the data access layer, not specific to Mongo
            - `/usecases`: code that actually executes the RecipeCart use cases and performs their logic
            - `/utils`: general utilities used by the other packages
    - `/test/java`: back-end tests: tests for each class in `src/main` will be located in a file indicating the same package (see `developer_manual.md` for more details).

# How to build the software
## First Steps
- Start by cloning the repository onto your local machine.
- Next follow the steps for "Building the back-end" and "Building the front-end".

## Building the back-end
The backend uses the Gradle build system to make sure that the backend builds correctly. Also, the backend uses **Java 11** as its Java version, so make sure you're using the correct version, or there may be errors building/running.
- Run `./gradlew` while on the top-level directory of the repository to run the Gradle wrapper, which installs/sets up Gradle for the project.
- Run `./gradlew build` to build the back-end.
    - If the build fails due to linting (i.e. in a task that starts with "`:spotless`"), then the "Linting" section below has more info on correcting formatting errors.
    - Though, build failures due to linting shouldn't happen on the `main` branch
### Running the back-end
- A JAR file is created when running `./gradlew build`.
    - This file is located in the `build/libs` directory (which is created when running `./gradlew build`), and has a name such as `CSE-403-RecipeCart-1.0-SNAPSHOT.jar`.
    - Run this JAR file with the command `java -jar build/libs/<filename>` (while in the top-level directory), where `<filename>` is the JAR file name.
        - This JAR executable also has optional command-line arguments, specified in the section below.
        - To exit the running program gracefully, type "quit" into stdin, which will stop the server and save changes as necessary, before exiting.
        - `./gradlew run` can also be used to run the backend, but typing "quit" will not do anything here as stdin gets "eaten" by Gradle.

#### Backend JAR executable command line arguments
```
usage: recipecart
 -d,--disable-final-save           Disable the final save to file when
                                   quitting this program
 -f,--filename <file>              The location/name of the file the
                                   entity data are saved in. Defaults to
                                   src/main/resources/entities.ser. If the
                                   file doesn't exist, the server starts
                                   with no entity data, and the file is
                                   created upon saving.
 -h,--help                         Print this message and exit.
 -m,--mock-data                    Pre-populate the entity data with some
                                   mock entity data. Pre-population can
                                   cause autosaving
 -p,--port <portnum>               The port for this server to listen in
                                   on. Defaults to 4567. Must be between
                                   1024 and 65535 inclusive.
 -u,--updates-per-autosave <num>   Entity data is autosaved to the file
                                   every [this argument] number of times.
                                   Must be a integer. Defaults to 1.
                                   Autosaving is disabled if set to 0.
```

### Linting
Also of note, is that this Gradle configuration uses the Spotless plugin to lint the code, specifically to conform to the Google Style Guide for Java.
It's configured so that the build will fail if there's any formatting errors.
There's a Gradle task `./gradlew spotlessApply` that automatically corrects these formatting errors.
Typically, when editing code, this task is done right before building.

### Javadoc
The Java classes the backend uses are documented with Javadoc notation. The Javadoc pages for these classes can be generated using `./gradlew javadoc` while in the top-level directory of this repository. The Javadocs are outputted in the `build/docs/javadoc` folder. In this folder, there will be an `index.html` file. Open this file in your web browser (recent versions of Firefox, Chrome, and Edge will work with this) to get the top-level page of the Javadocs.

## Building the front-end
- Install Node (v18) from [Node.js](https://nodejs.org/en/download/)
- Navigate to the `/front-end/` directory
- Run `npm install` to install all the dependencies.
- Run `npm run build` to build the front end.

### Running the front-end (development server)
- First, make sure the steps for building the front-end have been performed.
- Run `npm run start` (while in the `front-end` directory) to start the application.
- Access the website at `http://localhost:8080`

### Running the front-end (production server)
You will need to install Python 3 to run a production server.
- First, make sure the steps for building the front-end have been performed.
- Go to the `build` subdirectory within the `front-end` directory.
- Run `python -m http.server <port>` to start the server.
  - `<port>` is an optional argument, where the port number the server listens on is specified.
    - For example, `python -m http.server 8001` runs the frontend server and has it listen in on port 8001.
  - Depending on how Python is installed, the `python` keyword might need to be replaced with `python3`, `py`, or `py -3`.
- The program output will say where the website can be accessed (usually `http://localhost:8000`)


## Continuous Integration (CI)
- This repository uses GitHub Actions to perform continuous integration, in order to make sure that builds in the remote repository pass (for both the frontend and backend).
- A CI build occurs whenever there's a push or a pull request in the remote repository (that affects the frontend or backend files), on any branch. When merging a pull request, we make sure that the CI build passes before doing so.
- There are two GitHub Actions workflows used by our CI:
    - Backend workflow (`.github/workflows/gradle.yml`): this uses Gradle to build the back-end files, run tests on them, lint, etc. to make sure everything works there. Only changes that affect the back-end or Gradle will cause this workflow to run, and this workflow only looks at backend files (i.e. Gradle files and the `src` folder). This workflow runs all backend tests each time it's run.
    - Frontend workflow (`.github/workflows/node.js.yml`): this uses npm to build the front-end files, run tests on them, lint, etc. to make sure everything works there. Only changes that affect the front-end will cause this workflow to run, and this workflow only looks at frontend files (i.e. `front-end` folder). This workflow runs all frontend tests each time it's run.


# How to test the software
## Testing the back-end
- Running `./gradlew build` also runs the backend tests.
- Also, running `./gradlew test` just runs the tests without the other build steps.
- One other thing to note is that some exception stack-traces may be printed when running these tests, even if all tests pass. Do not be alarmed of this, as some tests test for if an exception is being handled, and printing the stacktrace of the exception is one of the behaviors that those exception handlers do. Just note if the tests pass or not.

## Testing the front-end
- Navigate to `/front-end/` directory
- Run `npm run test` to run tests.

## How to add new tests
### Backend
The backend is tested via JUnit 5's unit tests.
These tests are located in the `src/test` directory.
Conventionally, tests for a file in the `src/main` directory (where the backend source code is) are located in a file named `<filename>Test.java`, where `<filename>` is the name of the file (without the ".java") to be tested.
Also, conventionally, this test file is located in a package in the same name (the file and its test file are NOT in the same directory).
For example, if you have a file named `Foo.java` in the directory `src/main/com/recipecart/foo`, then its tests will be in the file `FooTest.java` in the directory `src/test/com/recipecart/foo`.
To actually add a new test, create a method in the correct test file (can be a new test file or an existing one), and annotate it with @Test.
This method will automatically be run along with the other tests.
Also, parameterized tests are heavily utilized in the unit tests, and they're annotated with @ParameterizedTest.
More info on JUnit parameterized tests (and JUnit tests in general) [here](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests).

### Frontend
The tests for the frontend are located in the `/front-end/tests/` directory. These tests are for JavaScript code and are run via Jest. The naming convention of `filename.test.js` must be followed for the tests to be run properly. To add a test, create a new file or add to an existing test file if relevant. Ensure React is imported, then create a test using the format `test('testName', () => {TEST HERE});`.

# How to build a release of the software
Building a release of the software involves the same steps as building the software (both frontend and backend), and running it on the server of your choice.
- For running the front-end, follow the steps for building a production server, not a development server.
- The server can be accessed remotely using the hostname of the machine running the server, and the port number given when running the frontend production server.
- The build does have to pass on both the machine running it and the remote repository on the corresponding branch (primarily `main`).
If it doesn't pass, the appropriate fixes must be made.

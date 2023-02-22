# How to obtain the source code
All source code for RecipeCart is located in this repository. The `backend-general` branch contains newer changes in the backend that have yet to be merged into the `main` branch.

# The layout of your directory structure.
The README.md file contains information on the directory structure of this repository, in the "Repository Layout" section.

# How to build the software.
The README.md file contains steps and information for building (both the frontend and backend of) this software, in the "Building the System" section.

# How to test the software.
The README.md file contains steps and information for testing (both the frontend and backend of) this software, in the "Testing the System" section.

# How to add new tests
### Backend
The backend is tested via JUnit unit tests.
These tests are located in the `src/test` directory.
Conventionally, tests for a file in the `src/main` directory (where the backend source code is) are located in a file named `<filename>Test.java`, where `<filename>` is the name of the file (without the ".java") to be tested.
Also, conventionally, this test file is located in a package in the same name (the file and its test file are NOT in the same directory).
For example, if you have a file named `Foo.java` in the directory `src/main/com/recipecart/foo`, then its tests will be in the file `FooTest.java` in the directory `src/test/com/recipecart/foo`.
To actually add a new test, create a method in the correct test file (can be a new test file or an existing one), and annotate it with @Test.
This method will automatically be run along with all of the other test.
Also, parameterized tests are heavily utilitzed in the unit tests, and they're annotated with @ParameterizedTest.
More info on JUnit parameterized tests (and JUnit tests in general) here: https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests.

### Frontend

# How to build a release of the software
Building a release of the software involves the same steps as building the software, and running it on the server of your choice.
But the build does have to pass on both the machine running it and the remote repository on the corresponding branch (primarily `main`).
If it doesn't pass, the appropriate fixes must be made.

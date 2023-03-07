# CSE-403-RecipeCart

# Introduction
RecipeCart an application where users can search, save, upload, and rate recipes, and ingredients for these recipes can be added to a built-in shopping list. A common problem that people face when cooking recipes is getting the right amount of ingredients based on what they already have in their selves and fridges. This aims to achieve a seamless integration of finding and cooking recipes, and shopping for (remaining) ingredients needed for these recipes. Technology-wise, the application will be a web app, where the user can login and get recipes and other information from a server.

### Other documents
- [`user_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/user_manual.md) contains documentation for a user to run a RecipeCart server.
- [`developer_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/developer_manual.md) contains documentation for a developer to contribute code to this repository.
- [`api_routes.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/api_routes.md) contains documentation on the protocols for communication between the frontend and backend

(All of these documents are located on the top-level directory of this repository.)
# Goals
## Major Features
- User-uploaded local storage of ingredients and amounts (manually)
  - Optional to the user; other features should work but may have less functionality
  - User would need to create an account/log in for this
- User-uploaded recipes (manually)
- Default database of recipes supplied to user
- Search recipes with toggle for already-acquired/needs-shopping
  - Filter recipes based on size, time, skill level, dietary requirements
- Grocery list that the user can add ingredients to
  - Add a recipe’s (missing) ingredients directly to it

## Stretch Goals
- Integration with Amazon/Walmart API for seamless ingredient ordering
- Automatically tracking user ingredients and amounts
- Web-crawl various recipe websites to add recipes to app

# Repository Layout
Main Folders: front-end, back-end, database, and reports  
- front-end: Front-end related files
- src: back-end related files
- database: databases of of our recipe, user, ingredient information
- reports: weekly updates on each collaboraters' progress and goals
- bug-tracking: Stores a file that keeps track of all our bugs

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

# Building the System
## First Steps
- Start by cloning the repository.
- Next follow the steps for "Building the back-end" and "Building the front-end".

## Building the back-end
The backend uses the Gradle build system to make sure that the backend builds correctly. Also, the backend uses **Java 11** as its Java version, so make sure you're using the correct version, or there may be errors building/running.
- Run `./gradlew` while on the top-level directory of the repository to run the Gradle wrapper, which installs/sets up Gradle for the project.
- Run `./gradlew build` to build the back-end.
#### Running the back-end
- A JAR file is created when running `./gradlew build`. This file is located in the `build/libs` directory (which is created when running `./gradlew build`), and has a name such as `CSE-403-RecipeCart-1.0-SNAPSHOT.jar`. Run this JAR file with the command `java -jar build/libs/<filename>` (while in the top-level directory), where `<filename>` is the JAR file name.
  - To exit the running program gracefully, type "quit" into stdin, which will stop the server and save changes as necessary, before exiting.
  - `./gradlew run` can also be used to run the backend, but typing "quit" will not do anything here as stdin gets "eaten" by Gradle.
### Linting
Also of note is that this Gradle configuration uses the Spotless plugin to lint the code, specifically to conform to the Google Style Guide for Java.
It's configured so that the build will fail if there's any formatting errors.
There's a Gradle task `./gradlew spotlessApply` that automatically corrects these formatting errors.
Typically when editing code, this task is done right before building.
## Building the front-end
- Install Node (v18) from [Node.js](https://nodejs.org/en/download/)
- Navigate to the `/front-end/` directory
- Run `npm install` to install all the dependencies.
- Run `npm run build` to build the front end.
- Run `npm run start` to start the application.
- Access the website at http://localhost:8080

# Continuous Integration (CI)
- This repository uses GitHub Actions to perform continuous integration, in order to make sure that builds in the remote repository pass (for both the frontend and backend).
- A CI build occurs whenever there's a push or a pull request in the remote repository (that affects the frontend or backend files), on any branch. When merging a pull request, we make sure that the CI build passes before doing so.
- There are two GitHub Actions workflows used by our CI:
  - Backend workflow (`.github/workflows/gradle.yml`): this uses Gradle to build the back-end files, run tests on them, lint, etc. to make sure everything works there. Only changes that affect the back-end or Gradle will cause this workflow to run, and this workflow only looks at backend files (i.e. Gradle files and the `src` folder). This workflow runs all backend tests each time it's run.
  - Frontend workflow (`.github/workflows/node.js.yml`): this uses npm to build the front-end files, run tests on them, lint, etc. to make sure everything works there. Only changes that affect the front-end will cause this workflow to run, and this workflow only looks at frontend files (i.e. `front-end` folder). This workflow runs all frontend tests each time it's run.
 
# Testing the System
## Testing the back-end
- Running `./gradlew build` also runs the backend tests.
- Also, running `./gradlew test` just runs the tests without the other build steps.
- One other thing to note is that some exception stacktrackes may be printed when running these tests, even if all tests pass. Do not be alarmed of this, as some tests test for if an exception is being handled, and printing the stacktrace of the exception is one of the behaviors that those exception handlers do. Just note if the tests pass or not.

## Testing the front-end
- Navigate to `/front-end/` directory
- Run `npm run test` to run tests.

# Functional Use Cases
- Currently, our only use cases that is operational is a user searching for a recipe and creating a recipe.

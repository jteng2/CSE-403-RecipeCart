# A high level description of RecipeCart
RecipeCart is a website that can manage recipes for many users. It can hold various information for different recipes, such as what ingredients and amounts are required, etc. It can also hold various information on users, such as the recipes they've saved, what ingredients and amounts they have in their shopping list, etc. A user would want to use this software in order to keep track of recipes better.
# How to install the software
1. Clone this repository onto your local machine.
2. Perform the rest of the steps for building the software as described in the README.md in the top-level directory of the repository.
# How to run (start) the software:
1. First make sure the software is installed.
2. Then, to run the backend part of the software, run the command `./gradlew run` in the same directory as `gradlew`. Or, if you're using IntelliJ, you can also do this Gradle task from the Gradle tab on the IntelliJ GUI. Another option for running the backend is with the .jar file. Building the software produces an executable .jar file, which will be located in the `build/libs` folder that's created after building. To run this .jar, go to this directory in your terminal, then run `java -jar <filename>`, where `<filename>` is the name of the .jar file. Make sure you have Java installed.
3. To run the frontend part of the software, navigate to the `/front-end/` directory and run `npm run build` followed by `npm run start` to launch the website. The website is accessible through a browser at `http://localhost:8080`.

# How to use the software
- After starting the software, connect to the website address that's specified in the README.md, via web browser. This will take you to the RecipeCart website, where the various use cases can be performed. (This website will already be connected to the backend.) Currently, the only use cases that are functional are searching for recipes and creating new recipes (the rest of the use-cases are not yet functional). The use cases we will have in the future include:
- Recipe Search
    - Search the database for recipes using the search bar
    - Filter by a variety of tags (WIP)
- Add Recipes to the Database
    - After logging in (WIP), add recipes to the local Database using the Add Recipe page (WIP)
- Save a Recipe
    - After logging in, save a recipe to visit it later through the Saved Recipes page (WIP)
- Rate a Recipe
    - After logging in, rate a recipe from your Saved Recipes page (WIP)
# How to report a bug
Currently, we are using GitHub Issues to track known bugs. To report a new bug, simply navigate to the Issues tab on the RecipeCart repository and create a new issue.

Information required to report a bug:
- Description of the bug
- Where did the bug happen?
- Expected outcome
- Actual outcome
- Steps to reproduce the unexpected outcome
- Any files they want to attach such as visual outcome of the bug

# Known bugs
For a detailed description of our known bugs, visit the GitHub Issues page of the repository.

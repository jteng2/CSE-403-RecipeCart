# CSE-403-RecipeCart

# Introduction
RecipeCart an application where users can search, save, upload, and rate recipes, and ingredients for these recipes can be added to a built-in shopping list. A common problem that people face when cooking recipes is getting the right amount of ingredients based on what they already have in their selves and fridges. This aims to achieve a seamless integration of finding and cooking recipes, and shopping for (remaining) ingredients needed for these recipes. Technology-wise, the application will be a web app, where the user can login and get recipes and other information from a server.

### Other documents
- [`user_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/user_manual.md) contains documentation for a user to use RecipeCart as a client and to report bugs.
- [`developer_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/developer_manual.md) contains documentation for a developer to run their own RecipeCart server, as well as contribute code to this repository.
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
The [`developer_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/developer_manual.md) file contains information on the directory structure of this repository, in the "Repository Layout" section.

# Building the System
The [`developer_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/developer_manual.md) file contains steps and information for building (both the frontend and backend of) this software, in the "How to build the software" section. That section also covers how to run the frontend and backend, and it covers the continuous integration system used.

# Testing the System
The [`developer_manual.md`](https://github.com/jteng2/CSE-403-RecipeCart/blob/main/developer_manual.md) file contains steps and information for testing (both the frontend and backend of) this software, in the "How to test the software" section. The section also covers how to add new tests to the system.

# Functional Use Cases
- Currently, our only use cases that is operational is a user searching for a recipe and creating a recipe.

# CSE-403-RecipeCart

# Introduction
RecipeCart an application where users can search, save, upload, and rate recipes, and ingredients for these recipes can be added to a built-in shopping list. A common problem that people face when cooking recipes is getting the right amount of ingredients based on what they already have in their selves and fridges. This aims to achieve a seamless integration of finding and cooking recipes, and shopping for (remaining) ingredients needed for these recipes. Technology-wise, the application will be a web app, where the user can login and get recipes and other information from a server.

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
front-end: Front-end related files  
back-end: back-end related files  
database: databases of of our recipe, user, ingredient information  
reports: weekly updates on each collaboraters' progress and goals
bug-tracking: Stores a file that keeps track of all our bugs

# Building the System
## First Steps
- Start by cloning the repository.
- Next follow the steps for "Building the back-end" and "Building the front-end".

## Building the back-end

## Building the front-end
- Run `npm install` to install all the dependencies.
- Run `npm run build` to build the front end.
- Run `npm run start` to start the application.
- Access the website at http://localhost:8080
 
# Testing the System

## Testing the back-end

## Testing the front-end
- Run `npm run test` to run tests.



# Functional Use Case
- Currently, our only use case that is operational is a user searching for a recipe.

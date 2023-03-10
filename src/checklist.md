# Feature checklist for backend

### Use cases
- [x] Implement the "Search for recipe" use case
    - [x] Write tests for SearchRecipesCommand
    - [x] Implement SearchRecipesCommand
    - [x] Write the API route for this use case
    - [x] Implement this API route in HttpRequestHandler
- [x] Implement the "Create recipe" use case
    - [x] Write tests for CreateRecipeCommand
    - [x] Implement CreateRecipeCommand
    - [x] Write the API route for this use case
    - [x] Implement this API route in HttpRequestHandler
- [x] Implement the "Save/bookmark recipe" use case
    - [x] Write tests for BookmarkRecipeCommand
    - [x] Implement BookmarkRecipeCommand
    - [x] Write the API route for this use case
    - [x] Implement this API route in HttpRequestHandler
- [x] Implement the "Add ingredients to shopping list" use case
    - [x] Write tests for AddIngredientsToShoppingListCommand
    - [x] Implement AddIngredientsToShoppingListCommand
    - [x] Write the API route for this use case
    - [x] Implement this API route in HttpRequestHandler
- [x] Implement the "Add recipe ingredients to shopping list" use case
    - [x] Write tests for AddRecipeToShoppingListCommand
    - [x] Implement AddRecipeToShoppingListCommand
    - [x] Write the API route for this use case
    - [x] Implement this API route in HttpRequestHandler

### Mongo database connectivity
- [x] Write tests for the data access layer
- [ ] Implement MongoClientKeeper
- [ ] Implement MongoConnector
  - [ ] Get an address of the database to connect to
- [ ] Implement MongoEntityLoader
- [ ] Implement MongoEntitySaver
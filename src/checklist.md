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
- [ ] Implement the "Save/bookmark recipe" use case
    - [ ] Write tests for BookmarkRecipeCommand
    - [ ] Implement BookmarkRecipeCommand
    - [ ] Write the API route for this use case
    - [ ] Implement this API route in HttpRequestHandler
- [ ] Implement the "Add ingredients to shopping list" use case
    - [ ] Write tests for AddIngredientsToShoppingListCommand
    - [ ] Implement AddIngredientsToShoppingListCommand
    - [ ] Write the API route for this use case
    - [ ] Implement this API route in HttpRequestHandler
- [ ] Implement the "Add recipe ingredients to shopping list" use case
    - [ ] Write tests for AddRecipeToShoppingListCommand
    - [ ] Implement AddRecipeToShoppingListCommand
    - [ ] Write the API route for this use case
    - [ ] Implement this API route in HttpRequestHandler

### Mongo database connectivity
- [x] Write tests for the data access layer
- [ ] Implement MongoClientKeeper
- [ ] Implement MongoConnector
  - [ ] Get an address of the database to connect to
- [ ] Implement MongoEntityLoader
- [ ] Implement MongoEntitySaver
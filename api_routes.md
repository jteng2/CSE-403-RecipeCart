# API Routes for front-end and back-end communication
## Search for recipe
This route is for searching for recipes (that aren't known yet to the user) based on given search terms.
### Header
```
GET /search/recipes HTTP/1.1
...
Accept: application/json
...
```
### Query parameters
`terms`: what the search terms are. Each search term is separated by a "+".
### Example request
`/search/recipes?terms=cheese+omelette` will perform a search with the terms "cheese" and "omelette".
### Response
A 200 status code will be returned if the search was successful, even if no recipes matched with the search terms. A 400 status code will be returned if no search terms are given (ex. just `search/recipes`). The body will be in JSON.  
The response will also contain a message with some details about what happened when handling the request (i.e. what error occurred if any, etc.)
### Example responses
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Search successful: recipes that matched were found",
    "matches": [
        {
            "name": "tasty_cheese_omelette2",
            "presentationName": "Tasty Cheese Omelette",
            "authorUsername": "OmeletteLover2000"
            "prepTime": 5,
            "cookTime": 5,
            "imageUri": "image/resource/here.png",
            "numServings": 1,
            "avgRating": 4.5,
            "numRatings": 20,
            "directions": [
                "Crack eggs into bowl",
                "Beat eggs",
                "Start stove with temperature on medium",
                ...
            ],
            "tags": [
                "breakfast",
                "quick",
                ...
            ],
            "requiredIngredients": {
                "egg": 2,
                "cheese": 0.75,
                ...
            }
        },
        ...
    ]
}
```
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Search successful: but no matching recipes were found",
    "recipes": []
}
```
Note that `prepTime` and `cookTime` are in minutes.
## Create recipe
This route is for creating new recipes to be saved into the data access layer.
### Header
```
POST /recipes/create HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the recipe details, as well as the name of the user that created the recipe (`authorUsername`). Only the `authorUsername` and `presentationName` are required, but if other fields are left out, then the recipe will have fewer details. If `name` is excluded or null, then a unique recipe name will be assigned to the recipe.
### Example body
```
{
    "encryptedJwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
    "recipe": {
        "name": "tasty_cheese_omelette2",
        "presentationName": "Tasty Cheese Omelette",
        "authorUsername": "OmeletteLover2000"
        "prepTime": 5,
        "cookTime": 5,
        "imageUri": "image/resource/here.png",
        "numServings": 1,
        "avgRating": 4.5,
        "numRatings": 20,
        "directions": [
            "Crack eggs into bowl",
            "Beat eggs",
            "Start stove with temperature on medium",
            ...
        ],
        "tags": [
            "breakfast",
            "quick",
            ...
        ],
        "requiredIngredients": {
            "egg": 2,
            "cheese": 0.75,
            ...
        }
    }
}
```
### Response
A 201 (Created) status code will be returned if the recipe was successfully created. The response will contain the unique recipe name assigned to the recipe (`assignedName`). A 400 (Bad request) status code will be returned if the recipe data is invalid. Recipe data is invalid if any of the following are true:
* The `presentationName`, `authorUsername`, `encryptedJwtToken`, or `recipe` is missing or null.
* A `name` is specified, but there already exists a recipe with the same `name`.
* Any of the `directions` elements are null.
* Any of the `tags` elements are null or don't correspond to existing tags.
* Any of the `requiredIngredients` keys are null or don't correspond to existing ingredients.
* Any of the `requiredIngredients` values are null.
* The `authorUsername` doesn't correspond to an existing user.

Note: the fields `directions`, `tags`, and `requiredIngredients` can be null themselves; they just can't have null elements. A 401 (Unauthorized) status code will be returned if the JWT token from `encryptedJwtToken` is invalid.  
The response will also contain a message with some details about what happened when handling the request (i.e. what error occurred if any, etc.)
### Example response
```
HTTP/1.1 201 Created
...
Content-type: application/json
...

{
    "message": "Recipe creation successful",
    "assignedName": "tasty_cheese_omelette2"
}
```
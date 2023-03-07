# API Routes for front-end and back-end communication
### General notes
* Access these API routes by connecting to `localhost:4567` while running the backend.
  * If running the backend with a non-default port (i.e. not 4567), then connect to `localhost` with the port used instead.
* The frontend sends these requests, and the backend returns the responses to these requests
* Each request body and response body is in JSON.
* Each request's response body will contain a `"message"` field with some details about what happened when handling the request (i.e. what error occurred if any, etc.)
## Table of Contents
1. [Get recipe](#get-recipe)
2. [Get user](#get-user)
3. [Get ingredient](#get-ingredient)
4. [Get tag](#get-tag)
5. [Create recipe](#create-recipe)
6. [Create user](#create-user)
7. [Create ingredient](#create-ingredient)
8. [Create tag](#create-tag)
9. [Search for recipe](#search-for-recipe)
10. [Bookmark recipe](#bookmark-recipe)
11. [Add ingredients to shopping list](#add-ingredients-to-shopping-list)
12. [Add recipe ingredients to shopping list](#add-recipe-ingredients-to-shopping-list)

<div id="get-recipe"></div>

## Get recipe
This route is for getting the information of an existing recipe.
### Header
```
GET /recipes/:recipe HTTP/1.1
...
Accept: application/json
...
```
### Header details
":recipe" is the recipe's unique (non-presentation) name. For example, if the recipe was "tasty-cheese-omelette2", then the URI would be `/recipes/tasty-cheese-omelette2`.
### Response
A 200 status code will be returned if the recipe was successfully retrieved.

A 404 status code will be returned if the recipe name doesn't correspond to an existing recipe.

Also, note that the users, ingredients, and tags are just their names, so additional requests will need to be made to get information on each of them.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Recipe retrieval successful",
    "recipe": {
        "name": "tasty-cheese-omelette2",
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
            "cheddar-cheese": 0.75,
            ...
    }
}
```
<div id="get-user"></div>

## Get user
This route is for getting the information of an existing user. 
### Header
```
GET /users/:user HTTP/1.1
...
Accept: application/json
...
```
### Header details
":user" is the user's username. For example, if the username was "OmeletteLover2000", then the URI would be `/users/OmeletteLover2000`.
### Response
A 200 status code will be returned if the user was successfully retrieved.

A 404 status code will be returned if the username doesn't correspond to an existing user.

Also, note that the recipes and ingredients are just their names, so additional requests will need to be made to get information on each of them.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "User retrieval successful",
    "user": {
        "username": "OmeletteLover2000",
        "emailAddress": "ExampleEmail@aol.com",
        "authoredRecipes": ["tasty-cheese-omelette2", "egg-surprise", "egg-in-baskets", ...],
        "savedRecipes": ["lovely-omelette", "zesty-omelette", ...],
        "ratedRecipes": {
            "lovely-omelette": 5.0,
            "over-easy-eggs": 1.0,
            ...
        },
        "ownedIngredients": ["eggs", "cheddar-cheese", ...],
        "shoppingList": {
            "eggs": 1000,
            "milk": 6,
            ...
        }
    }
}
```
<div id="get-ingredient"></div>

## Get ingredient
This route is for getting the information on an existing ingredient.
### Header
```
GET /ingredients/:ingredient HTTP/1.1
...
Accept: application/json
...
```
### Header details
":ingredient" is the ingredient's name. For example, if the name was "milk", then the URI would be `/ingredients/milk`.
### Response
A 200 status code will be returned if the ingredient was successfully retrieved.

A 404 status code will be returned if the ingredient doesn't correspond to an existing ingredient.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Ingredient retrieval successful",
    "ingredient": {
        "name": "milk"
        "units": "cups"
        "imageUri": milk/image/resource/here.png
    }
}
```
<div id="get-tag"></div>

## Get tag
This route is for getting the information on an existing tag. Currently, the only information on a tag is its name, but this route exists in case more information for tags gets added in the future.
### Header
```
GET /tags/:tag HTTP/1.1
...
Accept: application/json
...
```
### Header details
":tag" is the tag's name. For example, if the name was "breakfast", then the URI would be `/tags/breakfast`.
### Response
A 200 status code will be returned if the tag was successfully retrieved.

A 404 status code will be returned if the tag doesn't correspond to an existing tag.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Tag retrieval successful",
    "ingredient": {
        "name": "breakfast"
    }
}
```
<div id="create-recipe"></div>

## Create recipe
This route is for creating new recipes to be saved into the data access layer.
### Header
```
POST /create/recipe HTTP/1.1
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
            "cheddar-cheese": 0.75,
            ...
        }
    }
}
```
### Response
A 201 (Created) status code will be returned if the recipe was successfully created. The response will contain the unique recipe name assigned to the recipe (`assignedName`). If any of the tags don't correspond to an existing tag name, then new tags will be created for them, and they will be in the field `createdTags`.

A 400 (Bad request) status code will be returned if the recipe data is invalid. Recipe data is invalid if any of the following are true:
* The `presentationName`, `authorUsername`, `encryptedJwtToken`, or `recipe` is missing or null.
* A `name` is specified, but there already exists a recipe with the same `name`.
* Any of the `directions` elements are null.
* Any of the `tags` elements are null.
* Any of the `requiredIngredients` keys or values are null.

A 404 (Not found) status code will be returned if any of the following are true:
* The `authorUsername` doesn't correspond to an existing user.
* Any of the recipe's `requiredIngredients` keys don't correspond to existing ingredients.

A 401 (Unauthorized) status code will be returned if the JWT token from `encryptedJwtToken` is invalid (not yet implemented).

Note: the fields `directions`, `tags`, and `requiredIngredients` can be null themselves; they just can't have null elements.
### Example response
```
HTTP/1.1 201 Created
...
Content-type: application/json
...

{
    "message": "Recipe creation successful: the recipe was assigned a new unique (non-presentation) name",
    "assignedName": "tasty-cheese-omelette2",
    "createdTags": []
}
```
<div id="create-user"></div>

## Create user
This route is for creating a new user into the data access layer. This user won't start out with anything (i.e. they won't have any saved recipes, etc.).
### Header
```
POST /create/user HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the username of the user, and their email address. The email address is optional.
### Example body
```json
{
  "username": "OmeletteLover2000",
  "emailAddress": "ExampleEmail@aol.com"
}
```
### Response
A 201 (Created) status code will be returned if the user is successfully created.

A 400 (Bad request) status code will be returned if the username is null, missing, or already taken.
### Example response
```
HTTP/1.1 201 Created
...
Content-type: application/json
...

{
    "message": "User creation successful"
}
```
```
HTTP/1.1 400 Bad request
...
Content-type: application/json
...

{
    "message": "User creation unsuccessful: username was already taken"
}
```
<div id="create-ingredient"></div>

## Create ingredient
This route is for creating new ingredients to be saved into the data access layer.
### Header
```
POST /create/ingredient HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the ingredient name, what units it's measured in, and the URI of an image of it. The units are optional (for ingredients like "eggs"), and the image URI is optional.
### Example body
```json
{
    "name": "flour",
    "units": "cups",
    "imageUri": "[image uri goes here]"
}
```
### Response
A 201 (Created) status code will be returned if the ingredient is successfully created.

A 400 (Bad request) status code is returned if the name is null, missing, or already taken.
### Example response
```
HTTP/1.1 201 Created
...
Content-type: application/json
...

{
    "message": "Ingredient creation successful"
}
```
<div id="create-tag"></div>

## Create tag
This route is for creating new tags to be saved into the data access layer.
### Header
```
POST /create/tag HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the tag name. 
### Example body
```json
{
  "name": "breakfast"
}
```
### Response
A 201 (Created) status code will be returned if the tag is successfully created.

A 400 (Bad request) is returned if the name is null, missing, or already taken.
### Example response
```
HTTP/1.1 201 Created
...
Content-type: application/json
...

{
    "message": "Tag creation successful"
}
```
<div id="search-for-recipe"></div>

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
A 200 status code will be returned if the search was successful, even if no recipes matched with the search terms.

A 400 status code will be returned if no search terms are given (ex. just `search/recipes`). The body will be in JSON.
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
            "name": "tasty-cheese-omelette2",
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
                "cheddar-cheese": 0.75,
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
<div id="bookmark-recipe"></div>

## Bookmark recipe
This route is for having a user bookmark a recipe, so they can more easily retrieve it later.
### Header
```
POST /bookmark/recipe HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the username of the user, and the unique name of the recipe they want to save.
### Example body
```json
{
    "username": "OmeletteLover2000",
    "encryptedJwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
    "recipe": "zesty-omelette"
}
```
### Response
A 200 status code will be returned if the recipe was successfully bookmarked.

A 404 status code will be returned if the username or recipe name don't correspond to an existing user or recipe, respectively.

A 400 (Bad request) will be returned if any of the fields are null or missing, or if the user already has the recipe saved.

A 401 (Unauthorized) status code will be returned if the JWT token from `encryptedJwtToken` is invalid.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Recipe bookmarking successful"
}
```
<div id="add-ingredients-to-shopping-list"></div>

## Add ingredients to shopping list
This route is for having a user add ingredients to their shopping list.
### Header
```
POST /shopping-list/add-ingredients HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the username of the user, the ingredients they want to add to their shopping list, and the amounts for each ingredient that they want.
### Example body
```
{
  "username": "OmeletteLover2000",
  "encryptedJwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "ingredients": {
    "eggs": 24,
    "cheddar-cheese": 2,
    ...
  }
}
```
### Response
A 200 status code will be returned if the ingredients are successfully added to the user's shopping list.

A 404 status code will be returned if any of the ingredient names don't correspond to existing ingredients.

A 400 (Bad request) will be returned if any of the fields are null or missing, or if any keys/values of the ingredients are null.

A 401 (Unauthorized) status code will be returned if the JWT token from `encryptedJwtToken` is invalid.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Ingredients successfully added to shopping list"
}
```
<div id="add-recipe-ingredients-to-shopping-list"></div>

## Add recipe ingredients to shopping list
This route is for having a user add a recipe's ingredients to their shopping list.
### Header
```
POST /shopping-list/add-recipe-ingredients HTTP/1.1
...
Content-type: application/json
Accept: application/json
...
```
### Body details
The body will be JSON containing the username of the user, the recipe whose ingredients they want to add to their shopping list, and if they only want to add the recipe's ingredients that they're currently missing. If `addOnlyMissingIngredients` is missing or null, it will default to `false`.
### Example body
```
{
  "username": "OmeletteLover2000",
  "encryptedJwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "recipe": "lovely-omelette",
  "addOnlyMissingIngredients": true
}
```
### Response
A 200 status code will be returned if the recipe's ingredients are successfully added to the user's shopping list.

A 404 status code will be returned if the username or recipe name doesn't correspond to an existing user or recipe, respectively.

A 400 (Bad request) status code will be returned if any of the fields (except `addOnlyMissingIngredients`) are missing or null.

A 401 (Unauthorized) status code will be returned if the JWT token from encryptedJwtToken is invalid.
### Example response
```
HTTP/1.1 200 OK
...
Content-type: application/json
...

{
    "message": "Recipe's ingredients successfully added to shopping list"
}
```
import React from "react";
import SearchBar from "./SearchBar";
import RecipeEntry from "./RecipeEntry";
import RecipeBigTile from "./RecipeBigTile";
import "../resources/styles/RecipeSearchList.css"

async function get_recipes(searchQuery) {
    if (!searchQuery) {
        return { "matches": [] }
    }
    searchQuery = searchQuery.replace(' ', '+').toLowerCase();
    let url = `http://localhost:4567/search/recipes?terms=${searchQuery}`;
    try {
        let response = await fetch(url, {
            mode: 'cors'
        });
        return await response.json();
    } catch (error) {
        console.error(error);
        return {
            error: error.message
        };
    }
}

async function RecipeSearchList(props) {
    const populated = "Showing search results for: \"";
    let search = props?.search ?? "";
    let updateSearch = props?.updateSearch ?? "";
    async function find_recipes() {
        let search = props?.search;
        const recipes_data = await get_recipes(search);
        let matching_recipes = recipes_data["matches"];

        console.log(matching_recipes);
        let recipe_entries = []
        for (let i = 0; i < matching_recipes.length; i++) {
            const name = matching_recipes[i]["name"];
            const ingredients = matching_recipes[i]["ingredients"];
            const time_to_cook = matching_recipes[i]["time to cook"];
            recipe_entries[i] = <RecipeEntry key={name}
                recipeNumber={name}
                ingredients={ingredients}
                time_to_cook={time_to_cook}
                component={<RecipeBigTile name={name} />} />;
        }
        return recipe_entries;
    };

    let found_recipes = await find_recipes();
    return (
        <div className="search">
            <SearchBar updateSearch={updateSearch} />
            <p>{search ?
                populated + search + "\"" :
                "Enter a recipe to search"}
            </p>
            {search ?
                <ul className="recipe-list">
                    {found_recipes.map((recipeItem, index) => (
                        <li className="recipe-item" key={index}>{recipeItem}</li>
                    ))}
                </ul> : null}
        </div>
    );
} export default RecipeSearchList;

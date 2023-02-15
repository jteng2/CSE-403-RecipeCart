import React from "react";
import SearchBar from "./SearchBar";
import RecipeEntry from "./RecipeEntry";
import RecipeSmallBox from "./RecipeSmallBox";

function RecipeSearchList(props) {
    const populated = "Showing search results for: \"";
    let recipes = require("../resources/data/recipe-data.json");

    const find_recipes = () => {
        const matching_recipes = [];
        for (let i = 0; i < recipes["recipes"].length; i++) {
            props.search.split(" ").map((word) => {
                if (recipes["recipes"][i]["name"].includes(word)) {
                    if (!matching_recipes.includes(recipes["recipes"][i])) {
                        matching_recipes.push(recipes["recipes"][i]);
                    }
                }
            })
        }
        // matching_recipes.map((name) => {
        //     <li key={name}>{name}</li>
        // }) WHY DOES THIS NOT WORK???????

        console.log(matching_recipes);
        for (let i = 0; i < matching_recipes.length; i++) {
            const name = matching_recipes[i]["name"];
            const ingredients = matching_recipes[i]["ingredients"];
            const time_to_cook = matching_recipes[i]["time to cook"];
            matching_recipes[i] = <RecipeEntry recipeNumber={name}
                                     ingredients={ingredients}
                                     time_to_cook={time_to_cook}
                                     component={<RecipeSmallBox name={name}/>}/>;
        }
        return matching_recipes;
    };


    return (
        <div>
            <SearchBar updateSearch = {props.updateSearch}/>
            <p>{props.search ? 
                populated + props.search + "\"" :
                "Enter a recipe to search"}
            </p>
            {props.search ? <ul>{find_recipes()}</ul> : null}
        </div>
    );
} export default RecipeSearchList;

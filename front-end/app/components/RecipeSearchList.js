import React from "react";
import { useState, useEffect } from 'react';
import SearchBar from "./SearchBar";
import RecipeEntry from "./RecipeEntry";
import RecipeBigTile from "./RecipeBigTile";
import "../resources/styles/RecipeSearchList.css"

function RecipeSearchList(props) {
    const [matching, setMatching] = useState([]);

    const populated = "Showing search results for: \"";

    useEffect(() => {
        fetch(`http://localhost:4567/search/recipes?terms=${props.search}`)
            .then(res => res.json())
            .then((result) => setMatching(result["matches"]));
    }, []);


    let recipe_components = []
    for (let i = 0; i < matching.length; i++) {
        let recipe_name = matching[i]["name"];
        let presentation_name = matching[i]["presentationName"];
        let time_to_cook = matching[i]["cookTime"];
        let ingredients = Object.keys(matching[i]["requiredIngredients"]).join(", ");
        let directions = matching[i]["directions"].join(", ");

        recipe_components[i] = <RecipeEntry key={recipe_name}
            recipeName={presentation_name}
            time_to_cook={time_to_cook}
            component={<RecipeBigTile name={presentation_name} />}
            directions={directions}
            ingredients={ingredients}
        />
    }


    return (
        <div className="search">
            <SearchBar updateSearch={props.updateSearch} />
            <p>{props.search ?
                populated + props.search + "\"" :
                "Enter a recipe to search"}
            </p>
            {props.search ?
                <ul className="recipe-list">
                    {recipe_components.map((recipeItem, index) => (
                        <li className="recipe-item" key={index}>{recipeItem}</li>
                    ))}
                </ul> : null}
        </div>
    );
} export default RecipeSearchList;

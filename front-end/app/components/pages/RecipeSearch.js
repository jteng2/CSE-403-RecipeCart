import React from "react";
import Header from "../Header";
import { useLocation } from "react-router-dom";

function RecipeSearch() {
    const populated = "Showing search results for: \"";
    let location  = useLocation();

    return(
        <div>
            <p>RECIPE SEARCH</p>
            <Header />
            <p>{location.state ? 
                populated + location.state.result + "\"" :
                "Enter a recipe to search"}</p>
        </div>
    );
} export default RecipeSearch;

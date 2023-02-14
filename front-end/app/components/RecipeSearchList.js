import React from "react";
import SearchBar from "./SearchBar";

function RecipeSearchList(props) {
    const populated = "Showing search results for: \"";

    return (
        <div>
            <SearchBar updateSearch = {props.updateSearch}/>
            <p>{props.search ? 
                populated + props.search + "\"" :
                "Enter a recipe to search"}</p>
        </div>
    );
} export default RecipeSearchList;

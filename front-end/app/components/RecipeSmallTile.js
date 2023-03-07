import React from 'react';
import "./../resources/styles/RecipeSmallTile.css";

function RecipeSmallTile(props) {
    return (
        <div className="recipe-tile">
            {props.name ? props.name : "Recipe"}
        </div>
    );
}

export default RecipeSmallTile;
import React from 'react';
import "./../resources/styles/RecipeSmallBox.css";

function RecipeSmallBox(props) {
    return (
        <div className="recipe-box">
           {props.name ? props.name : "Recipe"}
        </div>
    );
}

export default RecipeSmallBox;
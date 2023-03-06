import React from 'react';
import "../resources/styles/RecipeBigTile.css";

const RecipeBigTile = (props) => {
  return (
    <div className="recipe-tile">
      {props.name ? props.name : "Recipe"}
    </div>
  );
}

export default RecipeBigTile;
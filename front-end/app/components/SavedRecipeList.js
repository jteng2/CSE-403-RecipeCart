import React, { useEffect, useState } from "react";
import Grid from "@mui/material/Grid";
import axios from "axios";
import RecipeEntry from "./RecipeEntry";
import RecipeBigTile from "./RecipeBigTile";

function SavedRecipeList(props) {
    const user = props.user;
    const [savedRecipes, setSavedRecipes] = useState([]);
    const [populatedRecipes, setPopulatedRecipes] = useState([]);

    const getRecipes = async () => {
        const response = await axios.get("http://localhost:4567/users/Author1");
        const recipes = response.data["user"]["authoredRecipes"];
        setSavedRecipes(recipes);
        recipes.forEach(async (recipe) => {
            console.log(recipe);
            const authored = await axios.get("http://localhost:4567/recipes/" + recipe);
            console.log(authored.data["recipe"]);
            setPopulatedRecipes(oldPopulated => [...oldPopulated, authored.data["recipe"]]);
        });
    };

    useEffect(() => {
        getRecipes();
    }, []);

    const formatRecipes = () => {
        const formattedRecipes = [];
        for (let i = 0; i < populatedRecipes.length; i++) {
            const name = populatedRecipes[i]["presentationName"];
            const ingredients = populatedRecipes[i]["requiredIngredients"];
            console.log(ingredients);
            const time_to_cook = populatedRecipes[i]["cookTime"];
            formattedRecipes[i] = <RecipeEntry key={name}
                recipeNumber={name}
                ingredients={ingredients}
                time_to_cook={time_to_cook}
                component={<RecipeBigTile name={name} />} />;
        }
        return formattedRecipes;
    }
    let formattedRecipes = formatRecipes();
    return (
        <Grid>
            {populatedRecipes ?
                <ul className="recipe-list">
                    {formattedRecipes.map((recipeItem, index) => (
                        <li className="recipe-item" key={index}>{recipeItem}</li>
                    ))}
                </ul> : null}
        </Grid>
    )
}
export default SavedRecipeList;

import React from "react";
import { useState } from "react";
import Grid from "@mui/system/Unstable_Grid/Grid";
import Button from "@mui/material";
import Header from "../Header";
import { TextField, Typography } from "@mui/material";

function AddRecipe() {
    const [name, setName] = useState("");
    const [ingredient, setIngredient] = useState("");
    const [ingredients, setIngredients] = useState([]);
    const [time_to_cook, setTime] = useState("");

    const handleIngredientSubmit = (input) => {
        setIngredients(oldIngredients => [...oldIngredients, input]);
        setIngredient("");
    };
    const handleRecipeSubmit = () => {
        //WE MAKE POST REQUEST TO API HERE
        setName("");
        setIngredient("");
        setTime("");

    };
    return(
        <Grid container>
            <Grid container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>ADD RECIPE</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
                <Grid item>
                    <Typography>Recipe Name</Typography>
                    <TextField
                        id="recipe-name"
                        value={name}
                        onChange={e => setName(e.target.value)}
                        placeholder="Name your Recipe"/>
                </Grid>
                <Grid item>
                    <Typography>Add Ingredients</Typography>
                    <TextField
                        id="add-ingredients"
                        value={ingredient}
                        onChange={e => setIngredient(e.target.value)}
                        placeholder="Add an Ingredient"/>
                </Grid>
                <Grid item>
                    <button onClick={handleIngredientSubmit}>Submit Ingredient</button>
                </Grid>
                <Grid item>
                    <Typography>Time to Cook</Typography>
                    <TextField
                        id="time-to-cook"
                        value={time_to_cook}
                        onChange={e => setTime(e.target.value)}
                        placeholder="Time to Cook"/>
                </Grid>
                <Grid item>
                    <button onClick={handleRecipeSubmit}>Submit your Recipe!</button>
                </Grid>
            </Grid>
        </Grid>
    );

} export default AddRecipe;
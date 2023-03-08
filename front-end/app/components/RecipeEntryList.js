import React from 'react';
import RecipeEntry from './RecipeEntry';
import Grid from "@mui/material/Grid";
import "./../resources/styles/RecipeEntryList.css";
import RecipeSmallTile from "./RecipeSmallTile";

const number = [1, 3, 5];

function RecipeEntryList() {
    return (
        <Grid container alignItems="center">
            {number.map((i, index) => {
                return (
                    <Grid key={index} container justifyContent="center" spacing={2} className="row">
                        <Grid item>
                            <RecipeEntry recipeNumber={i} component={<RecipeSmallTile />} />
                        </Grid>
                        <Grid item>
                            <RecipeEntry recipeNumber={i + 1} component={<RecipeSmallTile />} />
                        </Grid>
                    </Grid>
                )
            })}
        </Grid>
    );
}

export default RecipeEntryList;
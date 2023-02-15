import React from 'react';
import RecipeEntry from './RecipeEntry';
import Grid from "@mui/material/Grid";
import "./../resources/styles/RecipeEntryList.css";
import RecipeSmallBox from "./RecipeSmallBox";

const number = [1, 3, 5];

function RecipeEntryList() {
    return (
        <Grid container alignItems="center">
            {number.map(i => {
                return (
                <Grid container justifyContent="center" spacing={2} className="row">
                    <Grid item>
                        <RecipeEntry recipeNumber={i} component={<RecipeSmallBox />}/>
                    </Grid>
                    <Grid item>
                        <RecipeEntry recipeNumber={i+1} component={<RecipeSmallBox />}/>
                    </Grid>
                </Grid>
                )
            })}
        </Grid>
    );
}

export default RecipeEntryList;
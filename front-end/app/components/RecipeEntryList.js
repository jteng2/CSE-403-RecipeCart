import React from 'react';
import RecipeEntry from './RecipeEntry';
import Grid from "@mui/material/Grid";
import "./../resources/styles/RecipeEntryList.css";

function RecipeEntryList() {
    return (
        <Grid container alignItems="center">
            <Grid container justifyContent="center" spacing={2} className="row">
                <Grid item>
                    <RecipeEntry />
                </Grid>
                <Grid item>
                    <RecipeEntry />
                </Grid>
            </Grid>
            <Grid container justifyContent="center" spacing={2} className="row">
                <Grid item>
                    <RecipeEntry />
                </Grid>
                <Grid item>
                    <RecipeEntry />
                </Grid>
            </Grid>
            <Grid container justifyContent="center" spacing={2} className="row">
                <Grid item>
                    <RecipeEntry />
                </Grid>
                <Grid item>
                    <RecipeEntry />
                </Grid>
            </Grid>
        </Grid>
    );
}

export default RecipeEntryList;
import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";

function SavedRecipes() {
    return(
        <Grid container alignItems="center">
            <Grid container justifyContent="center">
                <Grid item>
                    <p>SAVED RECIPES</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
            </Grid>
        </Grid>
    );
} export default SavedRecipes;

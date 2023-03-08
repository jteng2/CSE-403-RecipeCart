import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";
import SavedRecipeList from "../SavedRecipeList";
import { useAuth0 } from "@auth0/auth0-react";

function SavedRecipes() {
    const { isAuthenticated, user } = useAuth0();
    return(
        <Grid container>
            <Grid container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>SAVED RECIPES</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
                <Grid item>
                    {isAuthenticated ? <SavedRecipeList user={user}/> 
                    :
                    <h1>Please Login to see Saved Recipes</h1>}
                </Grid>
            </Grid>
        </Grid>
    );
} export default SavedRecipes;

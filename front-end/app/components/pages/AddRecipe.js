import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";
import { useAuth0 } from "@auth0/auth0-react";
import RecipeForm from "../RecipeForm";

function AddRecipe() {
    const { isAuthenticated, user } = useAuth0();

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
                    {isAuthenticated ? <RecipeForm user={user}/> : <h1>Please Login to add a Recipe</h1>}
                </Grid>
            </Grid>
        </Grid>
    );

} export default AddRecipe;

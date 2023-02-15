import React from "react";
import { useState } from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";
import { useLocation } from "react-router-dom";
import RecipeSearchList from "../RecipeSearchList";

function RecipeSearch() {
    let location  = useLocation();
    const [search, setSearch] = useState(location.state ? location.state.result : "");


    return(
        <Grid container >
            <Grid container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>RECIPE SEARCH</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
                <Grid item sx={{paddingTop: "2rem"}}>
                    <RecipeSearchList search = {search} updateSearch={setSearch} />
                </Grid>
            </Grid>
        </Grid>
    );
} export default RecipeSearch;

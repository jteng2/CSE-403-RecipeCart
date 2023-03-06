import React from "react";
import { useNavigate } from "react-router-dom";
import Grid from "@mui/material/Grid";
import Header from "../Header";
import SearchBar from "../SearchBar";
import RecipeEntryList from "../RecipeEntryList";

function Home() {
    let navigate = useNavigate();
    const updateSearch = (input) => {
        navigate("/recipe-search", { state: { result: input } });
    }

    return (
        <Grid container>
            <Grid item container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>HOME PAGE</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
                <Grid item sx={{
                    paddingTop: "3rem",
                    paddingBottom: "2rem",
                }}>
                    <SearchBar updateSearch={updateSearch} />
                </Grid>
                <Grid item>
                    <RecipeEntryList />
                </Grid>
            </Grid>
        </Grid>
    );
}

export default Home;

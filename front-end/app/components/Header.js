import React from "react";
import { useNavigate } from "react-router-dom";
import { AppBar, Toolbar } from "@mui/material";
import Grid from "@mui/material/Grid";
import ProfileButton from "./ProfileButton";
import ShoppingListButton from "./ShoppingListButton";
import SavedRecipeButton from "./SavedRecipeButton";
import SearchBar from "./SearchBar";
import LogoButton from "./LogoButton";

function Header() {
    let navigate = useNavigate();
    const updateSearch = (input) => {
        navigate("/recipe-search", { state: {result: input} });
    };
    return (
        <AppBar position="static" style={{ background: "#DCD7EE"}}>
            <Toolbar>
                <Grid container>
                    <Grid item xs={3}>
                        <ShoppingListButton />
                        <SavedRecipeButton />
                    </Grid>
                    <Grid item xs="3">
                        <LogoButton />
                    </Grid>
                    <Grid item xs={3}>
                        <SearchBar updateSearch = {updateSearch}/>
                    </Grid>
                    <Grid item xs={3}>
                        <ProfileButton />
                    </Grid>
                </Grid>
            </Toolbar>
        </AppBar>
    );
}

export default Header;

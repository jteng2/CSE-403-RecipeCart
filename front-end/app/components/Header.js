import React from "react";
import { AppBar, Toolbar } from "@mui/material";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import ProfileButton from "./ProfileButton";
import ShoppingListButton from "./ShoppingListButton";
import SavedRecipeButton from "./SavedRecipeButton";
import SearchBar from "./SearchBar";
import LogoButton from "./LogoButton";

function Header() {
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
                        <SearchBar />
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

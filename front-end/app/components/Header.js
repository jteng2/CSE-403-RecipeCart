import React from "react";
import { AppBar, Toolbar } from "@mui/material";
import Grid from "@mui/material/Grid";
import ProfileButton from "./ProfileButton";
import ShoppingListButton from "./ShoppingListButton";
import SavedRecipeButton from "./SavedRecipeButton";
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
                    <Grid item xs={6}>
                        <LogoButton />
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

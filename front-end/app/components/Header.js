import React from "react";
import { AppBar, Toolbar } from "@mui/material";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import ProfileButton from "./ProfileButton";
import ShoppingListButton from "./ShoppingListButton";
import SavedRecipeButton from "./SavedRecipeButton";
<<<<<<< HEAD
import SearchBar from "./SearchBar";
=======
import LogoButton from "./LogoButton";
>>>>>>> main

function Header() {
    return (
        <AppBar position="static" style={{ background: "#DCD7EE"}}>
            <Toolbar>
                <Grid container>
                    <Grid item xs={3}>
                        <ShoppingListButton />
                        <SavedRecipeButton />
                    </Grid>
<<<<<<< HEAD
                    <Grid item xs={3}>
                        <Box
                            sx={{
                                width: 100,
                                height: 50,
                                backgroundColor: "#33E3EC",
                                borderRadius: "6px"
                            }}
                        >
                            <Typography color="black">Recipe Cart</Typography>
                        </Box>
=======
                    <Grid item xs="3">
                        <LogoButton />
>>>>>>> main
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

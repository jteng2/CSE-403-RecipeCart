import React from "react";
import { AppBar, Toolbar } from "@mui/material";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import ProfileIcon from "./ProfileIcon"


function Header() {
    return (
        <AppBar position="static" style={{ background: "#DCD7EE"}}>
            <Toolbar>
            <Grid container>
                <Grid item xs="3">
                    <button>Hamburger Menu</button>
                </Grid>
                <Grid item xs="3">
                    <Box
                        sx={{
                            width: 100,
                            height: 50,
                            backgroundColor: "#33E3EC",
                            borderRadius: "6px"
                        }}
                    ><Typography color="black">Recipe Cart</Typography></Box>
                </Grid>
                <Grid item xs="3">
                    <TextField variant="filled" color="info" focused></TextField>
                </Grid>
                <Grid item xs="3">
                    <ProfileIcon />
                </Grid>
            </Grid>
            </Toolbar>
        </AppBar>
    );
}

export default Header;

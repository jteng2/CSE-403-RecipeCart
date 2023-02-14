import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";

function NotFound() {
    return(
        <Grid container alignItems="center">
            <Grid container justifyContent="center">
                <Grid item>
                    <p>PAGE NOT FOUND</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
            </Grid>
        </Grid>
    );
} export default NotFound;

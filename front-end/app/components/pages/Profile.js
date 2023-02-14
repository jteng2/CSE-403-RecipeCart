import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";

function Profile() {
    return(
        <Grid container alignItems="center">
            <Grid container justifyContent="center">
                <Grid item>
                    <p>PROFILE PAGE</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
            </Grid>
        </Grid>
    );
} export default Profile;

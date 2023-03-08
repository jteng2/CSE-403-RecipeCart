import React, {useState} from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";
import ProfileInfo from "../ProfileInfo";
import { useAuth0 } from "@auth0/auth0-react";

function Profile() {
    const { user, isAuthenticated } = useAuth0();
    return(
        <Grid container>
            <Grid container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>PROFILE PAGE</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
                {isAuthenticated ? <ProfileInfo user={user}/> : <h1>Please Sign in to view Profile</h1>}
            </Grid>
        </Grid>
    );
} export default Profile;

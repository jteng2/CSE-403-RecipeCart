import React, {useState} from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";
import { useAuth0 } from "@auth0/auth0-react";

function Profile() {
    const { getAccessTokenSilently } = useAuth0();
    const getToken = async () => {
        const varia = await getAccessTokenSilently();
        console.log(varia);
    }
    return(
        <Grid container>
            <Grid container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>PROFILE PAGE</p>
                    <button onClick={() => getToken()}>Press</button>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
            </Grid>
        </Grid>
    );
} export default Profile;

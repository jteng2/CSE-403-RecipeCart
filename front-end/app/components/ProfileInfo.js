import React from "react";
import Grid from "@mui/material/Grid";
import { useAuth0 } from "@auth0/auth0-react";
import { Link } from "react-router-dom";


function ProfileInfo(props) {
    const user = props.user;
    const { logout } = useAuth0();

    return(
        <Grid container sx={{paddingLeft: "3rem", paddingTop:"2rem"}}>
            <Grid container alignItems="start" justifyContent="center" direction="column">
                <Grid item>
                    <img 
                    src={user.picture}/>
                </Grid>
                <Grid item>
                    <h2>{user.name}</h2>
                    <h2>{user.email}</h2>
                </Grid>
                <Grid item sx={{paddingBottom:"2rem"}}>
                    <Link to="/add-recipe">
                        <button>Add Recipe</button>
                    </Link>
                </Grid>
                <Grid item>
                    <button onClick={() => logout({
                        returnTo: window.location.origin,
                    })}>Logout</button>
                </Grid>
            </Grid>
        </Grid>
    );
}

export default ProfileInfo;

import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";

function ShoppingList() {
    return(
        <Grid container>
            <Grid container alignItems="center" justifyContent="center" direction="column">
                <Grid item>
                    <p>SHOPPING LIST</p>
                </Grid>
                <Grid item>
                    <Header />
                </Grid>
            </Grid>
        </Grid>
    );
} export default ShoppingList;

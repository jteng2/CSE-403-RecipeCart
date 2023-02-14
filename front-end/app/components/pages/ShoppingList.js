import React from "react";
import Grid from "@mui/material/Grid";
import Header from "../Header";

function ShoppingList() {
    return(
        <Grid container alignItems="center">
            <Grid container justifyContent="center">
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

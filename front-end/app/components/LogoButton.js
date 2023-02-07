import React from "react";
import { Link } from "react-router-dom"
import IconButton from '@mui/material/IconButton'
import Icon from './../resources/images/logo.png'
import './../resources/styles/LogoButton.css'

function LogoButton() {
    return (
        <Link to="/">
            <IconButton color="primary" fontSize="large" aria-label="go to saved recipes">
                <img src={Icon} alt="RecipeCart logo" className="logo"/>
            </IconButton>
        </Link>
    );
}

export default LogoButton;
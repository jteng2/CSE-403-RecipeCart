import React from "react";
import { Link } from "react-router-dom"
import IconButton from '@mui/material/IconButton';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';

function ShoppingListButton() {
    return (
        <Link to="/shopping-list">
            <IconButton color="primary" fontSize="large" aria-label="go to shopping list">
                <ShoppingCartIcon />
            </IconButton>
        </Link>
    );
}

export default ShoppingListButton;
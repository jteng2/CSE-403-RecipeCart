import React from "react";
import { Link } from "react-router-dom"
import IconButton from '@mui/material/IconButton';
import BookmarkIcon from '@mui/icons-material/Bookmark';

function SavedRecipeButton() {
    return (
        <Link to="/saved-recipes">
            <IconButton color="primary" fontSize="large" aria-label="go to saved recipes">
                <BookmarkIcon />
            </IconButton>
        </Link>
    );
}

export default SavedRecipeButton;
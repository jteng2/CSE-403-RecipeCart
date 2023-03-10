import React from "react";
import { useState } from "react";
import TextField from "@mui/material/TextField";
import Box from "@mui/system/Box";
import "../resources/styles/SearchBar.css"

function SearchBar(props) {
    const [search, setSearch] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();
        props.updateSearch(search);
    };

    return (
        <Box className="search-bar"
            component="form"
            onSubmit={handleSubmit}>
            <TextField
                id="recipe-search-bar"
                value={search}
                onChange={e => setSearch(e.target.value)}
                placeholder="Search for recipes..."
            />
        </Box>
    );
}

export default SearchBar;

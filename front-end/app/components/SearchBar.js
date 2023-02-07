import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import TextField from "@mui/material/TextField";
import Box from "@mui/system/Box";

function SearchBar() {
    const [search, setSearch] = useState("");
    const navigate = useNavigate();

    const handleSubmit = () => {
        navigate("/recipe-search", { state: { result: search } });
    };

    return (
        <Box
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
import React from "react";
import { useState } from "react";
import Header from "../Header";
import { useLocation } from "react-router-dom";
import RecipeSearchList from "../RecipeSearchList";

function RecipeSearch() {
    let location  = useLocation();
    const [search, setSearch] = useState(location.state ? location.state.result : "");


    return(
        <div>
            <p>RECIPE SEARCH</p>
            <Header />
            <RecipeSearchList search = {search} updateSearch={setSearch}/>
        </div>
    );
} export default RecipeSearch;

import React from "react";
import { useState } from "react";
import Grid from "@mui/material/Grid";
import MenuItem from "@mui/material/MenuItem";
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import InputLabel from '@mui/material/InputLabel';
import axios from "axios";
import { TextField, Typography } from "@mui/material";

function RecipeForm(props) {
    const user = props.user;
    const unitList = ["cups",
        "grams",
        "tablespoons",
        "teaspoons",
        "ounces",
        "units",
        "liters",
        "milligrams",
        "milliliters"];
    const [name, setName] = useState("");
    const [ingredient, setIngredient] = useState("");
    const [ingredients, setIngredients] = useState();
    const [amount, setAmount] = useState("");
    const [recipeTag, setRecipeTag] = useState("");
    const [tags, setTags] = useState([]);
    const [direction, setDirection] = useState("");
    const [directions, setDirections] = useState([]);
    const [unit, setUnit] = useState("");
    const [time_to_prep, setPrepTime] = useState("");
    const [time_to_cook, setCookTime] = useState("");
    const [numServings, setNumServings] = useState("");

    const handleIngredientSubmit = () => {
        if (unit && ingredient && amount) {
            const newIngredients = { ...ingredients};
            newIngredients[ingredient.toLowerCase()] = parseInt(amount);
            setIngredients(newIngredients);
            setIngredient("");
            setAmount("");
            console.log(ingredients);
        } else {
            alert("You must select a unit for your ingredient!");
        }
    };    
    
    const handleDirectionSubmit = () => {
        if (direction) {
            setDirections(oldDirections => [...oldDirections, direction.toLowerCase()]);
            setDirection("");
            console.log(directions);
        }
    };
    const handleTagSubmit = () => {
        if (recipeTag) {
            setTags(oldTags => [...oldTags, recipeTag.toLowerCase()]);
            setRecipeTag("");
            console.log(tags);
        }
    };
    
    const handleUnit = (event) => {
        setUnit(event.target.value.toLowerCase());
    }
    const handleRecipeSubmit = () => {
        const recipe = {
            recipe: {
                presentationName: name,
                authorUsername: user.nickname,
                prepTime: parseInt(time_to_prep),
                cookTime: parseInt(time_to_cook),
                numServings: parseInt(numServings),
                directions: directions,
                tags: tags,
                requiredIngredients: ingredients
            }
        }
        axios.post('http://localhost:4567/create/recipe', { recipe })
        .then(res => {
            console.log(res);
        })

        setName("");
        setCookTime("");
        setPrepTime("");
        setNumServings("");
        setIngredients([]);
        setDirections([]);
        setTags([]);


    };

    return (
        <Grid container alignItems="center" justifyContent="center" direction="column">
            <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Recipe Name</Typography>
                    <TextField
                        id="recipe-name"
                        value={name}
                        onChange={e => setName(e.target.value.toLowerCase())}
                        placeholder="Name your Recipe"/>
                </Grid>
                <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Add Ingredients</Typography>
                    <TextField
                        id="add-ingredients"
                        value={ingredient}
                        onChange={e => setIngredient(e.target.value.toLowerCase())}
                        placeholder="Add an Ingredient"/>
                    <TextField
                        id="add-amount"
                        value={amount}
                        onChange={e => setAmount(e.target.value.toLowerCase())}
                        placeholder="Add Ingredient Amount"/>
                    <FormControl variant="standard" sx={{ m: 1, minWidth: 120 }}>
                        <InputLabel id="demo-simple-select-standard-label">Units</InputLabel>
                        <Select
                            labelId="demo-simple-select-standard-label"
                            id="demo-simple-select-standard"
                            value={unit}
                            onChange={handleUnit}
                            label="Units"
                        >
                        {unitList.map((unit) => {
                            return <MenuItem key={unit} value={unit}>{unit}</MenuItem>;
                        })}
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item>
                    <button onClick={handleIngredientSubmit}>Submit Ingredient</button>
                </Grid>
                <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Time to Prep</Typography>
                    <TextField
                        id="time-to-prep"
                        value={time_to_prep}
                        onChange={e => setPrepTime(e.target.value.toLowerCase())}
                        placeholder="Time to Prep"/>
                </Grid>
                <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Time to Cook</Typography>
                    <TextField
                        id="time-to-cook"
                        value={time_to_cook}
                        onChange={e => setCookTime(e.target.value.toLowerCase())}
                        placeholder="Time to Cook"/>
                </Grid>
                <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Number of Servings</Typography>
                    <TextField
                        id="num-servings"
                        value={numServings}
                        onChange={e => setNumServings(e.target.value.toLowerCase())}
                        placeholder="Number of servings"/>
                </Grid>
                <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Directions to Cook</Typography>
                    <TextField
                        id="directions"
                        value={direction}
                        onChange={e => setDirection(e.target.value.toLowerCase())}
                        placeholder="Directions"/>
                </Grid>
                <Grid item>
                    <button onClick={handleDirectionSubmit}>Submit Direction</button>
                </Grid>
                <Grid item sx={{paddingTop: "1rem"}}>
                    <Typography>Add Recipe Tags</Typography>
                    <TextField
                        id="tags"
                        value={recipeTag}
                        onChange={e => setRecipeTag(e.target.value.toLowerCase())}
                        placeholder="Directions"/>
                </Grid>
                <Grid item>
                    <button onClick={handleTagSubmit}>Submit Tag</button>
                </Grid>
                <Grid item sx={{paddingTop: "2rem"}}>
                    <button onClick={handleRecipeSubmit}>Submit your Recipe!</button>
                </Grid>
        </Grid>
    );
}
export default RecipeForm;

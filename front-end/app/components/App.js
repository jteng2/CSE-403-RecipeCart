import React from 'react';
import { lazy, Suspense } from 'react';
import { Routes, Route } from "react-router-dom";

const Home = lazy(() => import("./pages/Home"));
const Profile = lazy(() => import("./pages/Profile"));
const RecipeSearch = lazy(() => import("./pages/RecipeSearch"));
const SavedRecipes = lazy(() => import("./pages/SavedRecipes"));
const ShoppingList = lazy(() => import("./pages/ShoppingList"));
const NotFound = lazy(() => import("./pages/NotFound"));


function App() {
    return (
      <div>
        <Suspense fallback={<div className="container">LOADING...</div>}>
          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route path="/recipe-search" element={<RecipeSearch />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/saved-recipes" element={<SavedRecipes />} />
            <Route path="/shopping-list" element={<ShoppingList />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Suspense>
      </div>
    );
  }
  
  export default App;
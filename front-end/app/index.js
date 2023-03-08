import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import { Auth0Provider } from "@auth0/auth0-react";
import App from "./components/App";

ReactDOM.render(
    <React.StrictMode>
      <BrowserRouter>
        <Auth0Provider
          domain="dev-pwa58nltvew2lltr.us.auth0.com"
          clientId="vJ6ZeIlgkE9U9DnZ32bhNLLyFCNKfN59"
          redirectUri={window.location.origin}>
          <App />
        </Auth0Provider>
      </BrowserRouter>
    </React.StrictMode>,
    document.getElementById("root")
  );

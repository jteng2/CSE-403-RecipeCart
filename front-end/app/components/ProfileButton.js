import React from 'react';
import Button from '@mui/material/Button';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { Link } from "react-router-dom";
import { useAuth0 } from '@auth0/auth0-react';

function ProfileButton() {
  const { loginWithRedirect } = useAuth0();
  const { isAuthenticated } = useAuth0();


  return (
    <div>
      {isAuthenticated ? 
        <Link to="/profile">
          <AccountCircleIcon fontSize='large' color='action'/>
        </Link> 
        :
        <Button onClick={() => loginWithRedirect()}>Sign In</Button>
      }
    </div>
  );
}

export default ProfileButton;

import * as React from 'react';
import Button from '@mui/material/Button';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

function ProfileIcon() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleOption = () => {
    // TODO
  }

  return (
    <div>
      <Button
        id="profile-button"
        aria-controls={open ? 'profile-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        onClick={handleClick}
      >
        <AccountCircleIcon fontSize='large' color='action'/>
      </Button>
      <Menu
        id="profile-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          'aria-labelledby': 'profile-button',
        }}
      >
        <MenuItem onClick={handleOption}>My Account</MenuItem>
        <MenuItem onClick={handleOption}>Settings</MenuItem>
        <MenuItem onClick={handleOption}>Sign Up/Sign In</MenuItem>
        <MenuItem onClick={handleOption}>Logout</MenuItem>
      </Menu>
    </div>
  );
}

export default ProfileIcon;

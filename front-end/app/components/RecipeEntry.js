import * as React from 'react';
import PropTypes from 'prop-types';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Typography from '@mui/material/Typography';

const RecipeDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(2),
  },
  '& .MuiDialogActions-root': {
    padding: theme.spacing(1),
  },
}));

function RecipeDialogTitle(props) {
  const { children, onClose, ...other } = props;

  return (
    <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
      {children}
      {onClose ? (
        <IconButton
          aria-label="close"
          onClick={onClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
      ) : null}
    </DialogTitle>
  );
}

RecipeDialogTitle.propTypes = {
  children: PropTypes.node,
  onClose: PropTypes.func.isRequired,
};

export default function RecipeEntry(props) {
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <Button onClick={handleClickOpen}>
        {props.component}
      </Button>
      <RecipeDialog
        onClose={handleClose}
        aria-labelledby="recipe-dialogue"
        open={open}
      >
        <RecipeDialogTitle id="recipe-dialogue-name" onClose={handleClose}>
          Recipe Name: {props.recipeNumber}
        </RecipeDialogTitle>
        <DialogContent dividers>
          <Typography gutterBottom>
            Ingredients: Food, food, food, food, food, food, food, food
          </Typography>
          <Typography gutterBottom>
            Directions: Step 1, Step 2, Step 3, Step 4, Step 5, Step 6
          </Typography>
          <Typography gutterBottom>
            Prep: 20 minutes
          </Typography>
        </DialogContent>
      </RecipeDialog>
    </div>
  );
}
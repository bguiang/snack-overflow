import React from "react";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import useSnack from "../../hooks/useSnack";
import useStyles from "../../styles";
import {
  TextField,
  CardMedia,
  Button,
  IconButton,
  InputAdornment,
  Card,
  Typography,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  useMediaQuery,
} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import SnackOverflow from "../../api/SnackOverflow";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router-dom";
import DeleteForeverIcon from "@material-ui/icons/DeleteForever";
import { useTheme } from "@material-ui/core/styles";

const EditProduct = () => {
  const classes = useStyles();
  const history = useHistory();
  let { id } = useParams();
  const [snack] = useSnack(id);
  const { currentUser } = useAuth();
  const [token, setToken] = useState(null);

  const [name, setName] = useState("");
  const [images, setImages] = useState([]);
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [open, setOpen] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("xs"));

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSuccessMessage("");
    try {
      const updateProductRequest = {
        ...snack,
        name,
        images,
        description,
        price,
      };
      const response = await SnackOverflow.put(
        "/admin/products",
        updateProductRequest,
        {
          headers: { Authorization: token },
        }
      );

      if (200 === response.status) {
        setSuccessMessage("Update Success!");
      }
    } catch (error) {
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        setErrorMessage(error.response.data.errors[0].defaultMessage);
      } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        setErrorMessage("Something went wrong. Try again later");
      } else {
        // Something happened in setting up the request that triggered an Error
        setErrorMessage(error.message);
      }
    }
  };

  useEffect(() => {
    if (snack !== null && snack.images) {
      setName(snack.name);
      setImages(snack.images);
      setDescription(snack.description);
      setPrice(snack.price);
    }
  }, [snack]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setConfirmId("");
  };

  const [confirmId, setConfirmId] = useState("");
  const [confirmIdError, setConfirmIdError] = useState("");

  const deleteProduct = async () => {
    try {
      const response = await SnackOverflow.delete(`/admin/products/${id}`, {
        headers: { Authorization: token },
      });

      if (200 === response.status) {
        history.push("/admin/products");
      }
    } catch (error) {
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        setConfirmIdError(error.response.data.errors[0].defaultMessage);
      } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        setConfirmIdError("Something went wrong. Try again later");
      } else {
        // Something happened in setting up the request that triggered an Error
        setConfirmIdError(error.message);
      }
    }
  };

  const handleConfirmDelete = (event) => {
    event.preventDefault();
    setConfirmIdError("");
    if (confirmId == snack.id) {
      deleteProduct();
    } else {
      setConfirmIdError("ID does not match match");
    }
  };

  if (snack.deleted) {
    return (
      <div>
        <div className={classes.cartHeader}>
          <h2 className={classes.error}>Product #{id} no longer exists</h2>
        </div>
      </div>
    );
  }
  return (
    <div>
      <div className={classes.cartHeader}>
        <h2 className={classes.cartHeaderTitle}>Product #{id}</h2>
        <Button
          className={classes.error}
          size="small"
          onClick={handleClickOpen}
          startIcon={<DeleteForeverIcon />}
        >
          Delete
        </Button>
        <Dialog
          fullScreen={fullScreen}
          open={open}
          onClose={handleClose}
          aria-labelledby="responsive-dialog-title"
        >
          <DialogTitle id="responsive-dialog-title">
            {`Confirm delete by entering the Product ID`}
          </DialogTitle>
          <DialogContent>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="confirmId"
              label={`Enter '${id}' to confirm`}
              name="confirmId"
              autoComplete="false"
              onChange={(event) => {
                setConfirmId(event.target.value);
              }}
              helperText={confirmIdError}
              error={confirmIdError ? true : false}
            />
          </DialogContent>
          <DialogActions>
            <Button autoFocus onClick={handleClose} color="primary">
              Cancel
            </Button>
            <Button onClick={handleConfirmDelete} color="primary" autoFocus>
              Confirm
            </Button>
          </DialogActions>
        </Dialog>
      </div>

      <form className={classes.form} onSubmit={handleSubmit} noValidate>
        <TextField
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="name"
          label="Name"
          name="name"
          autoComplete="false"
          value={name}
          onChange={(event) => {
            setName(event.target.value);
          }}
        />
        <Images images={images} setImages={setImages} />
        <TextField
          variant="outlined"
          margin="normal"
          required
          fullWidth
          multiline
          rows={4}
          id="description"
          label="Description"
          name="description"
          autoComplete="false"
          value={description}
          onChange={(event) => {
            setDescription(event.target.value);
          }}
        />
        <TextField
          variant="outlined"
          margin="normal"
          type="number"
          required
          fullWidth
          id="price"
          label="Price"
          name="price"
          autoComplete="false"
          value={price}
          InputProps={{
            startAdornment: <InputAdornment position="start">$</InputAdornment>,
          }}
          onChange={(event) => {
            setPrice(event.target.value);
          }}
        />

        <Button
          type="submit"
          fullWidth
          variant="contained"
          color="primary"
          className={classes.submit}
        >
          Submit
        </Button>
        <Typography variant="h6" className={classes.success}>
          {successMessage}
        </Typography>
        <Typography variant="h6" className={classes.error}>
          {errorMessage}
        </Typography>
      </form>
    </div>
  );
};

const Images = ({ images, setImages }) => {
  const classes = useStyles();

  const addMoreClick = () => {
    setImages((images) => [...images, ""]);
  };

  const deleteClick = (index) => {
    let arrayUpdate = [...images];
    arrayUpdate.splice(index, 1);
    setImages(arrayUpdate);
  };

  const updateImage = (index, value) => {
    let arrayUpdate = [...images];
    arrayUpdate[index] = value;
    setImages(arrayUpdate);
  };

  return (
    <div className={classes.imageSection}>
      {images.map((image, index) => (
        <Card title={index} className={classes.editImageContainer} key={index}>
          <div className={classes.editImageContainerImageContainer}>
            <CardMedia
              image={images[index]}
              className={classes.editImageInputImage}
              title={"Image Preview"}
            />
          </div>
          <div className={classes.editImageInputTextInput}>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="imageUrl"
              label="Image Url"
              name="imageUrl"
              autoComplete="false"
              value={images[index]}
              onChange={(event) => {
                updateImage(index, event.target.value);
              }}
            />
            <IconButton onClick={() => deleteClick(index)}>
              <DeleteIcon color="error" />
            </IconButton>
          </div>
        </Card>
      ))}
      <div className={classes.deleteImageContainer}>
        <Button
          variant="outlined"
          color="primary"
          className={classes.submit}
          onClick={() => addMoreClick()}
        >
          Add Image
        </Button>
      </div>
    </div>
  );
};

export default EditProduct;

import React from "react";
import { useState, useEffect } from "react";
import useStyles from "../../styles";
import {
  TextField,
  CardMedia,
  Button,
  IconButton,
  InputAdornment,
  Card,
  Typography,
} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import SnackOverflow from "../../api/SnackOverflow";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router-dom";

const CreateProduct = () => {
  const classes = useStyles();
  const history = useHistory();
  const { currentUser } = useAuth();
  const [token, setToken] = useState(null);

  const [name, setName] = useState("");
  const [images, setImages] = useState([]);
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [categories, setCategories] = useState([]);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSuccessMessage("");
    setErrorMessage("");
    try {
      const updateProductRequest = {
        name,
        images,
        description,
        price,
        categories,
      };
      const response = await SnackOverflow.post(
        "/admin/products",
        updateProductRequest,
        {
          headers: { Authorization: token },
        }
      );

      if (201 === response.status) {
        setSuccessMessage("Update Success!");
        history.push(`/admin/products/${response.data.id}`);
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

  return (
    <div>
      <h2 className={classes.cartHeaderTitle}>Create New Product</h2>
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

export default CreateProduct;

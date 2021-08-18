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
} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import SnackOverflow from "../../api/SnackOverflow";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router-dom";

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
      console.log(updateProductRequest);
      const response = await SnackOverflow.put(
        "/admin/products",
        updateProductRequest,
        {
          headers: { Authorization: token },
        }
      );

      if (200 === response.status) {
        console.log("REPONSE 200");
        console.log(response.data);
        setSuccessMessage("Update Success!");
      }
    } catch (error) {
      console.log(error);
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

  return (
    <div>
      <h2 className={classes.cartHeaderTitle}>Edit Product</h2>
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
            console.log("Price changed: " + event.target.value);
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

  console.log(images.length);

  return (
    <div className={classes.imageSection}>
      {images.map((image, index) => (
        <Card title={index} className={classes.editImageContainer}>
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
            <IconButton>
              <DeleteIcon color="error" onClick={() => deleteClick(index)} />
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

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
} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";

const EditProduct = () => {
  const classes = useStyles();
  let { id } = useParams();
  const [snack] = useSnack(id);

  const handleSubmit = () => {};

  const [name, setName] = useState("");
  const [images, setImages] = useState([]);
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);

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
      <h2 className={classes.cartHeaderTitle}>Edit</h2>
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
          // onChange={(event) => {
          //   setUsername(event.target.value);
          // }}
          //   autoFocus
          //   helperText={usernameError}
          //   error={usernameError ? true : false}
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
        />
        <TextField
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="price"
          label="Price"
          name="price"
          autoComplete="false"
          value={price.toFixed(2)}
          InputProps={{
            startAdornment: <InputAdornment position="start">$</InputAdornment>,
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

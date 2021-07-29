import React, { useState, useEffect } from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import useStyles from "../../styles";
import { useAuth } from "../../context/AuthContext";
import SnackOverflow from "../../api/SnackOverflow";
import { useHistory, useLocation } from "react-router-dom";
import validator from "validator";

const LoginSignup = () => {
  const { currentUser, login } = useAuth();
  const classes = useStyles();
  return (
    <div className={classes.loginSignUp}>
      <Login login={login} classes={classes} currentUser={currentUser} />
      <div className={classes.flexLineBetween}></div>
      <SignUp classes={classes} />
    </div>
  );
};

const Login = ({ login, classes, currentUser }) => {
  let history = useHistory();
  let location = useLocation();
  let { from } = location.state || { from: { pathname: "/" } };
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // Send User to landing page "/" or to previous route if it was a private route
  const callback = () => {
    history.replace(from);
  };

  useEffect(() => {
    if (currentUser !== null) {
      callback();
    }
  }, [currentUser]);

  const [usernameError, setUsernameError] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const isValidated = () => {
    let isValid = true;

    // Reset Errors
    setUsernameError("");
    setPasswordError("");

    // Username
    if (validator.isEmpty(username)) {
      setUsernameError("Please enter a username");
      isValid = false;
    }

    if (validator.isEmpty(password)) {
      setPasswordError("Please enter your password");
      isValid = false;
    }

    return isValid;
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (isValidated()) login(username, password);
  };
  return (
    <Container component="main" maxWidth="xs">
      <div className={classes.paper}>
        <Typography component="h1" variant="h5">
          Login
        </Typography>
        <form className={classes.form} onSubmit={handleSubmit} noValidate>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="username1"
            label="Username"
            name="username"
            onChange={(event) => {
              setUsername(event.target.value);
            }}
            autoFocus
            helperText={usernameError}
            error={usernameError ? true : false}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password1"
            onChange={(event) => {
              setPassword(event.target.value);
            }}
            autoComplete="current-password"
            onChange={(event) => {
              setPassword(event.target.value);
            }}
            helperText={passwordError}
            error={passwordError ? true : false}
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
          <Grid container>
            <Grid item xs>
              <Link href="#" variant="body2">
                Forgot password?
              </Link>
            </Grid>
          </Grid>
        </form>
      </div>
    </Container>
  );
};

const SignUp = ({ classes }) => {
  const [signupSuccess, setSignupSuccess] = useState(false);
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [passwordRepeat, setPasswordRepeat] = useState("");

  const [fullNameError, setFullNameError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [passwordRepeatError, setPasswordRepeatError] = useState("");

  const isValidated = () => {
    let isValid = true;
    // Reset Errors
    setEmailError("");
    setFullNameError("");
    setUsernameError("");
    setPasswordError("");
    setPasswordRepeatError("");

    // Email
    if (!validator.isEmail(email)) {
      console.log("Invalid Email: " + email);
      setEmailError("Please use a valid email");
      isValid = false;
    }

    // Full Name
    if (validator.isEmpty(fullName, { ignore_whitespace: true })) {
      setFullNameError("Please enter your full name");
      isValid = false;
    }

    // Username
    if (
      !validator.isAlphanumeric(username) ||
      validator.contains(username, " ") ||
      !validator.isLength(username, { min: 6, max: 20 })
    ) {
      setUsernameError(
        "Username must be 6-20 characters long and contain only letters and numbers and no spaces"
      );
      isValid = false;
    }

    if (
      !validator.isStrongPassword(password, {
        minLength: 6,
        minLowercase: 1,
        minUppercase: 1,
        minNumbers: 1,
        minSymbols: 1,
      }) ||
      validator.contains(password, " ") ||
      !validator.isLength(password, { min: 6, max: 20 })
    ) {
      setPasswordError(
        "Password must be 6-20 characters long including at least one lowercase letter, one uppercase letter, one number, and one symbol. No spaces"
      );
      isValid = false;
    }

    // PasswordRepeat
    if (!password === passwordRepeat) {
      setPasswordRepeatError("Passwords do not match");
      isValid = false;
    }

    return isValid;
  };

  const [signupError, setSignupError] = useState("");
  const signup = async (fullName, email, username, password) => {
    const signupRequest = { fullName, email, username, password };

    try {
      const response = await SnackOverflow.post("/auth/signup", signupRequest);
      if (response.status === 201) {
        setSignupSuccess(true);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setSignupSuccess(false);
    if (isValidated()) {
      signup(fullName, email, username, password);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <div className={classes.paper}>
        <Typography component="h1" variant="h5">
          Sign Up
        </Typography>
        <form className={classes.form} onSubmit={handleSubmit} noValidate>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            onChange={(event) => {
              setEmail(event.target.value);
            }}
            helperText={emailError}
            error={emailError ? true : false}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="full-name"
            label="Full Name"
            id="full-name"
            onChange={(event) => {
              setFullName(event.target.value);
            }}
            helperText={fullNameError}
            error={fullNameError ? true : false}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="username"
            label="Username"
            id="username"
            onChange={(event) => {
              setUsername(event.target.value);
            }}
            helperText={usernameError}
            error={usernameError ? true : false}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            onChange={(event) => {
              setPassword(event.target.value);
            }}
            helperText={passwordError}
            error={passwordError ? true : false}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Repeat Password"
            type="password"
            id="repeat-password"
            onChange={(event) => {
              setPasswordRepeat(event.target.value);
            }}
            helperText={passwordRepeatError}
            error={passwordRepeatError ? true : false}
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
    </Container>
  );
};

export default LoginSignup;

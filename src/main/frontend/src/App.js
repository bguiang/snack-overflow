import "./App.css";
import { useState } from "react";
import "@fontsource/roboto";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Snacks from "./components/Snacks";
import Snack from "./components/Snack";
import Footer from "./components/Footer";
import LoginSignup from "./components/LoginSignup";
import Account from "./components/Account";
import { CssBaseline, Container } from "@material-ui/core";
import {
  ThemeProvider,
  createTheme,
  makeStyles,
} from "@material-ui/core/styles";
import { AuthProvider } from "./context/AuthContext";
import PrivateRoute from "./components/PrivateRoute";
import { CartProvider } from "./context/CartContext";
import Cart from "./components/Cart";

function App() {
  // Theme colors
  // Blue: #00B1C6 or rgb(0,177,198)
  // Yellow: #F8EB37 or rgb(248,235,55)
  // Orange: #F3AB1C or rgb(243,171,28)
  const theme = createTheme({
    palette: {
      primary: {
        main: "#00B1C6",
      },
      secondary: {
        main: "#F8EB37",
      },
    },
  });

  // Not using the styles.js because the theme created isn't being applied to the classes in App.js
  // The theme does get applied to useStyles from styles.js when used on child components
  const useStyles = makeStyles((theme) => ({
    app: {
      minHeight: "100vh",
      display: "flex",
      flexDirection: "column",
    },
    main: {
      paddingTop: 186,
      backgroundImage: `url(${process.env.PUBLIC_URL + "/pattern.png"})`,
      display: "flex",
      flex: 1,
    },
    container: {
      backgroundColor: theme.palette.background.paper,
      padding: 20,
    },
  }));

  const classes = useStyles();

  return (
    <AuthProvider>
      <ThemeProvider theme={theme}>
        <CartProvider>
          <CssBaseline />
          <Router>
            <div className={classes.app}>
              <Navbar />
              <main className={classes.main}>
                <Container className={classes.container} maxWidth="lg">
                  <Switch>
                    <Route path="/snacks/:id">
                      <Snack />
                    </Route>
                    <Route path="/about">
                      <Snack />
                    </Route>
                    <Route path="/contact">
                      <Snack />
                    </Route>
                    <Route path="/login">
                      <LoginSignup />
                    </Route>
                    <Route path="/cart">
                      <Cart />
                    </Route>
                    <PrivateRoute path="/account">
                      <Account />
                    </PrivateRoute>
                    <Route path="/">
                      <Snacks />
                    </Route>
                  </Switch>
                </Container>
              </main>
              <Footer />
            </div>
          </Router>
        </CartProvider>
      </ThemeProvider>
    </AuthProvider>
  );
}

export default App;

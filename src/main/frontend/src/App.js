import "./App.css";
import React, { useEffect } from "react";
import "@fontsource/roboto";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { CssBaseline, Container } from "@material-ui/core";
import {
  ThemeProvider,
  createTheme,
  makeStyles,
} from "@material-ui/core/styles";
import { AuthProvider } from "./context/AuthContext";
import PrivateRoute from "./components/PrivateRoute";
import { CartProvider } from "./context/CartContext";

import Navbar from "./components/Navbar";
import Snacks from "./components/SnackSearch/Snacks";
import Snack from "./components/SnackPage/Snack";
import Footer from "./components/Footer";
import LoginSignup from "./components/Account/LoginSignup";
import Account from "./components/Account/Account";
import Cart from "./components/Cart/Cart";
import Checkout from "./components/Checkout/Checkout";
import CheckoutSuccess from "./components/Checkout/CheckoutSuccess";
import CartRoute from "./components/Checkout/CheckoutRoute";
import Contact from "./components/Contact/Contact";
import Order from "./components/Account/Order";
import AdminRoute from "./components/AdminRoute";
import Admin from "./components/Admin/Admin";
import Home from "./components/Home/Home";

function App() {
  // Theme colors
  // Blue: #00B1C6 or rgb(0,177,198)
  // Yellow: #F8EB37 or rgb(248,235,55)
  // Orange: #F3AB1C or rgb(243,171,28)
  const theme = createTheme({
    palette: {
      primary: {
        main: "#00B1C6",
        contrastText: "#fff",
      },
      secondary: {
        main: "#F8EB37",
      },
      success: {
        main: "#4caf50",
      },
    },
  });

  useEffect(() => {
    document.title = "SnackOverflow";
  }, []);

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
      // padding: 20,
      padding: 0,
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
                    <Route path="/snacks">
                      <Snacks />
                    </Route>
                    <Route path="/contact">
                      <Contact />
                    </Route>
                    <Route path="/login">
                      <LoginSignup />
                    </Route>
                    <Route path="/cart">
                      <Cart />
                    </Route>
                    <AdminRoute path="/admin">
                      <Admin />
                    </AdminRoute>
                    <PrivateRoute path="/account/orders/:id">
                      <Order />
                    </PrivateRoute>
                    <PrivateRoute path="/account">
                      <Account />
                    </PrivateRoute>
                    <PrivateRoute path="/checkout/success">
                      <CheckoutSuccess />
                    </PrivateRoute>
                    <PrivateRoute path="/checkout">
                      <CartRoute path="/checkout">
                        <Checkout />
                      </CartRoute>
                    </PrivateRoute>
                    <Route path="/">
                      <Home />
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

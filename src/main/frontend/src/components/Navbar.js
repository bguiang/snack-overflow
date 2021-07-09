import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  useHistory,
  Link,
} from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  TextField,
  Container,
  Button,
  InputBase,
  Paper,
  IconButton,
  Menu,
  MenuItem,
} from "@material-ui/core";
import ShoppingCartIcon from "@material-ui/icons/ShoppingCart";
import MenuIcon from "@material-ui/icons/Menu";
import useStyles from "../styles";

import FacebookIcon from "@material-ui/icons/Facebook";
import InstagramIcon from "@material-ui/icons/Instagram";
import TwitterIcon from "@material-ui/icons/Twitter";
import YouTubeIcon from "@material-ui/icons/YouTube";
import LinkedInIcon from "@material-ui/icons/LinkedIn";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import SearchIcon from "@material-ui/icons/Search";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
  const classes = useStyles();
  const history = useHistory();
  const { currentUser } = useAuth();
  const accountButtonText = currentUser ? currentUser.username : "Account";

  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleClick = (url) => {
    history.push(url);
  };

  return (
    <AppBar position="fixed" className={classes.appbar}>
      <Container maxWidth="lg" className={classes.toolbarContainer}>
        <Toolbar className={classes.toolbar}>
          <Box className={classes.socialsMenu}>
            <IconButton href={"https://www.facebook.com"} target="_blank">
              <FacebookIcon />
            </IconButton>
            <IconButton href={"https://www.instagram.com"} target="_blank">
              <InstagramIcon />
            </IconButton>
            <IconButton href={"https://twitter.com"} target="_blank">
              <TwitterIcon />
            </IconButton>
            <IconButton href={"https://www.youtube.com"} target="_blank">
              <YouTubeIcon />
            </IconButton>
            <IconButton href={"https://www.linkedin.com"} target="_blank">
              <LinkedInIcon />
            </IconButton>
          </Box>
          <Box className={classes.logoContainer}>
            <img
              onClick={() => handleClick("/")}
              className={classes.logo}
              src={`${process.env.PUBLIC_URL}/assets/images/logo2.png`}
            />
          </Box>
          <Box className={classes.toolbarMenu}>
            <Button size="large" className={classes.margin}>
              <ShoppingCartIcon />
              Cart (0)
            </Button>
            <Button
              onClick={() => handleClick("/account")}
              size="large"
              className={classes.margin}
            >
              <AccountCircleIcon />
              {accountButtonText}
            </Button>
          </Box>
          <Box className={classes.toolbarMenuMobile}>
            <Button size="large" className={classes.shoppingCartButton}>
              <ShoppingCartIcon />
              (0)
            </Button>
            <Button
              onClick={() => handleClick("/account")}
              size="large"
              className={classes.margin}
            >
              <AccountCircleIcon />
            </Button>
          </Box>
        </Toolbar>
      </Container>
      <div className={classes.bottomToolbar}>
        <Container className={classes.toolbarContainer}>
          <Toolbar className={classes.toolbar2}>
            <Box className={classes.toolbar2Menu}>
              <Button
                onClick={() => handleClick("/")}
                size="large"
                className={classes.toolbar2MenuItem}
              >
                Home
              </Button>
              <Button
                onClick={() => handleClick("/")}
                size="large"
                className={classes.toolbar2MenuItem}
              >
                Snacks
              </Button>
              <Button
                onClick={() => handleClick("/contact")}
                size="large"
                className={classes.toolbar2MenuItem}
              >
                Subscriptions
              </Button>
              <Button
                onClick={() => handleClick("/contact")}
                size="large"
                className={classes.toolbar2MenuItem}
              >
                Contact Us
              </Button>
              <Menu
                id="mobile-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                <MenuItem onClick={handleMenuClose}>Snacks</MenuItem>
                <MenuItem onClick={handleMenuClose}>Subscriptions</MenuItem>
                <MenuItem onClick={handleMenuClose}>Contact</MenuItem>
              </Menu>
            </Box>
            <Box className={classes.toolbar2MenuMobile}>
              <IconButton
                className={classes.menuButton}
                aria-label="mobile-menu"
                onClick={handleMenuClick}
              >
                <MenuIcon />
              </IconButton>
              <Menu
                id="mobile-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                <MenuItem onClick={handleMenuClose}>Home</MenuItem>
                <MenuItem onClick={handleMenuClose}>Snacks</MenuItem>
                <MenuItem onClick={handleMenuClose}>Subscriptions</MenuItem>
                <MenuItem onClick={handleMenuClose}>Contact</MenuItem>
              </Menu>
            </Box>
            <Paper className={classes.toolbar2SearchContainer}>
              <InputBase
                //flexGrow={1}
                className={classes.search}
                placeholder="Search Snacks"
                inputProps={{ "aria-label": "search snacks" }}
              />
              <IconButton
                type="submit"
                className={classes.iconButton}
                aria-label="search"
              >
                <SearchIcon />
              </IconButton>
            </Paper>
          </Toolbar>
        </Container>
      </div>
    </AppBar>
  );
};

export default Navbar;

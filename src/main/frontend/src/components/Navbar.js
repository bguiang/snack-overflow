import React, {useState} from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useHistory
} from "react-router-dom";
import { AppBar, Toolbar, Typography, Box, Container } from "@material-ui/core";
import {PhotoCamera} from '@material-ui/icons';

const Navbar = () => {
    const [active, setActive] = useState('snacks');
    
    const handleClick = e => {
        console.log('click ', e);
        setActive(e.key);
        //useHistory.push();
    };

    return (
        <AppBar position="relative">
            <Toolbar>
                <PhotoCamera/>
                <Typography variant="h6">
                    <Link to="/snacks" onClick={handleClick}>
                        Snacks
                    </Link>
                    <Link to="/about" onClick={handleClick} >
                        About
                    </Link>
                    <Link to="/contact" onClick={handleClick}>
                        Contact
                    </Link>
                </Typography>
            </Toolbar>
        </AppBar>
      );
}

export default Navbar;
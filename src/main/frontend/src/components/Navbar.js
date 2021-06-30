import React, {useState} from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useHistory
} from "react-router-dom";
import { AppBar, Toolbar, Typography, Box, TextField, Container, Button } from "@material-ui/core";
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import useStyles from "../styles";

const Navbar = () => {
    const classes = useStyles();
    const [active, setActive] = useState('snacks');
    const history = useHistory();
    
    const handleClick = url => {
        //console.log('click ', e);
        //setActive(e.key);
        history.push(url);
    };

    return (
        <AppBar position="relative" className={classes.appbar}>
            <div className={classes.appbarToolbarTop}>
                <Container maxWidth='lg'>
                    <Toolbar>
                        <Box>
                            <img
                                onClick={() => handleClick("/")}
                                style={{height: '80px', paddingTop: 10, paddingBottom: 10}}
                                src={'https://upload.wikimedia.org/wikipedia/commons/f/f7/Stack_Overflow_logo.png'}
                            />
                        </Box>
                        <Box flexGrow={1} color="red" display="flex" justifyContent="center" alignItems="center">
                            <form noValidate autoComplete="off">
                                <TextField id="outlined-basic" label="Search" variant="outlined" />
                            </form>
                        </Box>
                        <Box>
                            <Button size="large" className={classes.margin}>
                            Small
                            </Button>
                            <Button size="large" className={classes.margin}>
                             <ShoppingCartIcon/>Cart (0)
                            </Button>
                            <Button size="large" className={classes.margin}>
                            Login
                            </Button>
                        </Box>
                    </Toolbar>
                </Container>
            </div>
            <div className={classes.appbarToolbarMenu}>
                <Container>
                    <Toolbar>
                        <Button onClick={() => handleClick("/")} size="large" className={classes.margin}>
                            Snacks
                        </Button>
                        <Button onClick={() => handleClick("/about")} size="large" className={classes.margin}>
                            About
                        </Button>
                        <Button onClick={() => handleClick("/contact")} size="large" className={classes.margin}>
                            Contact
                        </Button>
                    </Toolbar>
                </Container>
            </div>
        </AppBar>
      );
}

export default Navbar;
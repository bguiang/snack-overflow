import React from "react";
import useStyles from "../../styles";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  useHistory,
} from "react-router-dom";
import {
  Button,
  Grid,
  Paper,
  Card,
  CardContent,
  Typography,
  CardActions,
} from "@material-ui/core";
import DashboardIcon from "@material-ui/icons/Dashboard";
import StorefrontIcon from "@material-ui/icons/Storefront";
import PeopleIcon from "@material-ui/icons/People";
import ListAltIcon from "@material-ui/icons/ListAlt";
import MonetizationOnIcon from "@material-ui/icons/MonetizationOn";
import BookIcon from "@material-ui/icons/Book";
import Dashboard from "./Dashboard";
import Orders from "./Orders";
import Transactions from "./Orders";
import ProductsAdmin from "./ProductsAdmin";
import Members from "./Members";
import Logs from "./Logs";

const Admin = () => {
  const classes = useStyles();
  const history = useHistory();

  const handleClick = (url) => {
    history.push(url);
  };

  return (
    <div className={classes.dashboard}>
      <div className={classes.dashboardMenu}>
        <Button
          onClick={() => handleClick("/admin")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<DashboardIcon />}
        >
          Dashboard
        </Button>
        <Button
          onClick={() => handleClick("/admin/orders")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<ListAltIcon />}
        >
          Orders
        </Button>
        <Button
          onClick={() => handleClick("/admin/transactions")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<MonetizationOnIcon />}
        >
          Transactions
        </Button>
        <Button
          onClick={() => handleClick("/admin/products")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<StorefrontIcon />}
        >
          Products
        </Button>
        <Button
          onClick={() => handleClick("/admin/members")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<PeopleIcon />}
        >
          Members
        </Button>
        <Button
          onClick={() => handleClick("/admin/logs")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<BookIcon />}
        >
          Logs
        </Button>
      </div>
      <div className={classes.dashboardMenuMobile}>
        <Button
          onClick={() => handleClick("/admin")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<DashboardIcon />}
        ></Button>
        <Button
          onClick={() => handleClick("/admin/orders")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<ListAltIcon />}
        ></Button>
        <Button
          onClick={() => handleClick("/admin/transactions")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<MonetizationOnIcon />}
        ></Button>
        <Button
          onClick={() => handleClick("/admin/products")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<StorefrontIcon />}
        ></Button>
        <Button
          onClick={() => handleClick("/admin/members")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<PeopleIcon />}
        ></Button>
        <Button
          onClick={() => handleClick("/admin/logs")}
          size="large"
          className={classes.dashboardMenuItem}
          startIcon={<BookIcon />}
        ></Button>
      </div>
      <div className={classes.dashboardContent}>
        <Switch>
          <Route path="/admin/orders">
            <Orders />
          </Route>
          <Route path="/admin/transactions">
            <Transactions />
          </Route>
          <Route path="/admin/products">
            <ProductsAdmin />
          </Route>
          <Route path="/admin/members">
            <Members />
          </Route>
          <Route path="/admin/logs">
            <Logs />
          </Route>
          <Route path="/admin">
            <Dashboard />
          </Route>
        </Switch>
      </div>
    </div>
  );
};

export default Admin;

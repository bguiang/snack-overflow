import React from "react";
import useStyles from "../../styles";
import {
  Button,
  Grid,
  Card,
  CardContent,
  Typography,
  CardActions,
} from "@material-ui/core";

const Dashboard = () => {
  const classes = useStyles();
  const handleClick = () => {};
  return (
    <Grid container spacing={2} justifyContent="center" alignItems="center">
      <Grid item xs={4} key="ordersThisMonth">
        <Card className={classes.root}>
          <CardContent>
            <Typography
              className={classes.title}
              color="textSecondary"
              gutterBottom
            >
              Orders This Month
            </Typography>
            <Typography variant="h5" component="h2">
              9999
            </Typography>
          </CardContent>
          <CardActions>
            <Button size="small">See Details</Button>
          </CardActions>
        </Card>
      </Grid>
      <Grid item xs={4} key="totalIncomeThisMonth">
        <Card className={classes.root}>
          <CardContent>
            <Typography
              className={classes.title}
              color="textSecondary"
              gutterBottom
            >
              Total Income This Month
            </Typography>
            <Typography variant="h5" component="h2">
              $9000.01
            </Typography>
          </CardContent>
          <CardActions>
            <Button size="small">See Details</Button>
          </CardActions>
        </Card>
      </Grid>
      <Grid item xs={4} key="newUsersThisMonth">
        <Card className={classes.root}>
          <CardContent>
            <Typography
              className={classes.title}
              color="textSecondary"
              gutterBottom
            >
              New Users This Month
            </Typography>
            <Typography variant="h5" component="h2">
              125
            </Typography>
          </CardContent>
          <CardActions>
            <Button size="small">See Details</Button>
          </CardActions>
        </Card>
      </Grid>

      <Grid item xs={12} key="topSellingProducts">
        <Card className={classes.root}>
          <CardContent>
            <Typography
              className={classes.title}
              color="textSecondary"
              gutterBottom
            >
              Top Selling Products This Month
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
          </CardContent>
          <CardActions>
            <Button size="small">Learn More</Button>
          </CardActions>
        </Card>
      </Grid>
    </Grid>
  );
};

export default Dashboard;

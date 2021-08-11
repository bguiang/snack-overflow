import React from "react";
import useStyles from "../../styles";
import { Grid, Typography, Card, CardActionArea } from "@material-ui/core";
import { useHistory } from "react-router-dom";

const MemberCard = ({ member }) => {
  const classes = useStyles();
  const history = useHistory();

  const itemClick = (id) => {
    history.push(`/admin/members/${id}`);
  };
  return (
    <Grid item xs={12} key={member.id}>
      <Card className={classes.orderCardMobile}>
        <CardActionArea
          onClick={() => itemClick(member.id)}
          className={classes.orderCardActionAreaMobile}
        >
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            ID: {member.id}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Username: {member.username}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            Full Name: {member.fullName}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Role: {member.role}
          </Typography>
          {/* <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            Joined: {new Date(order.createdDate).toLocaleDateString("en-US")}{" "}
            {new Date(order.createdDate).toLocaleTimeString("en-US")}
          </Typography> */}
        </CardActionArea>
      </Card>
      <Card className={classes.orderCard}>
        <CardActionArea
          onClick={() => itemClick(member.id)}
          className={classes.orderCardActionArea}
        >
          <Typography
            variant="subtitle2"
            className={classes.orderCardActionAreaItem}
          >
            {member.id}
          </Typography>
          <Typography
            variant="subtitle2"
            className={classes.orderCardActionAreaItem2}
          >
            {member.username}
          </Typography>
          <Typography
            variant="subtitle2"
            className={classes.orderCardActionAreaItem2}
          >
            {member.fullName}
          </Typography>
          <Typography
            variant="subtitle2"
            className={classes.orderCardActionAreaItem2}
          >
            {member.role}
          </Typography>
          {/* <Typography
            variant="subtitle2"
            className={classes.orderCardActionAreaItem2}
          >
            {new Date(order.createdDate).toLocaleDateString("en-US")}{" "}
            {new Date(order.createdDate).toLocaleTimeString("en-US")}
          </Typography> */}
        </CardActionArea>
      </Card>
    </Grid>
  );
};

export default MemberCard;

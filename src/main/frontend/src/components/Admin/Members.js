import React from "react";
import { Button, Card, CardActionArea, Typography } from "@material-ui/core";
import { Grid } from "@material-ui/core";
import useStyles from "../../styles";
import useMembers from "../../hooks/useMembers";
import MemberCard from "./MemberCard";

const Members = () => {
  const [members, getMembers] = useMembers();
  const classes = useStyles();
  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="orderInfo">
          <Typography variant="h6" className={classes.orderListTitleMobile}>
            Orders
          </Typography>
          <Grid
            item
            xs={12}
            key={"orderListTitle"}
            className={classes.orderListTitle}
          >
            <div className={classes.orderCard}>
              <div className={classes.orderCardActionArea}>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  ID
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem2}
                >
                  Username
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem2}
                >
                  Full Name
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem2}
                >
                  Role
                </Typography>
              </div>
            </div>
          </Grid>
          {members.map((member) => (
            <MemberCard member={member} />
          ))}
        </Grid>
      </Grid>
    </div>
  );
};

export default Members;

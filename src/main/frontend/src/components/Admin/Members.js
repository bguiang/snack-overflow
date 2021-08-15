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
    <Grid
      container
      justifyContent="center"
      alignItems="center"
      justifyContent="flex-start"
      alignItems="flex-start"
    >
      <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
        <h2 className={classes.cartHeaderTitle}>Members</h2>
      </Grid>
      <Grid container xs={12} spacing={1} key="memberInfo">
        <Grid item xs={12} key={"listTitle"}>
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
          <MemberCard member={member} key={member.id} />
        ))}
      </Grid>
    </Grid>
  );
};

export default Members;

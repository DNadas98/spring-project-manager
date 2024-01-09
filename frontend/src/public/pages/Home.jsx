import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {Button, Collapse, Container, Grid, List, ListItem, ListItemIcon, ListItemText, Typography} from '@mui/material';
import {styled} from '@mui/system';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import FollowTheSignsIcon from '@mui/icons-material/FollowTheSigns';
import HardwareIcon from '@mui/icons-material/Hardware';

const StyledTypographyMain = styled(Typography)(({theme}) => ({
  fontWeight: 'bold',
  fontSize: '4rem',
  color: '#FFFFFF',
  fontFamily: 'Arial, sans-serif',
  marginBottom: theme.spacing(2),
}));

const StyledTypographySub = styled(Typography)(({theme}) => ({
  fontSize: '2rem',
  color: '#FFFFFF',
  marginBottom: theme.spacing(3),
}));

const StyledTypographySteps = styled(Typography)(({theme}) => ({
  fontSize: '1.5rem',
  marginTop: theme.spacing(3),
  marginBottom: theme.spacing(3),
}));

const StyledButton = styled(Button)(({theme}) => ({
  marginRight: theme.spacing(2),
  color: 'inherit',
}));

const StyledDiv = styled('div')(({theme}) => ({
  background: 'linear-gradient(45deg, #516d79 30%, #11508d 90%)',
  color: '#FFFFFF',
  accentColor: '#FFFFFF',
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2)
}));

const Home = () => {
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    setChecked(true);
  }, []);

  return (
    <Container>
      <Grid container spacing={4} justifyContent="center">
        <Grid item xs={12}>
          <StyledDiv>
            <StyledTypographyMain variant="h1">
              TASX
            </StyledTypographyMain>
            <StyledTypographySub variant="h1">
              Project management gamified
            </StyledTypographySub>
            <StyledTypographySub variant="h3">
              <HardwareIcon/> Got something to work ON?
              <br/>
              <FollowTheSignsIcon/> Get something to work FOR!
            </StyledTypographySub>
            <Collapse in={checked} {...(checked ? {timeout: 1000} : {})}>
              <StyledTypographySteps variant="h3">
                <List>
                  <ListItem>
                    <ListItemIcon>
                      <ArrowForwardIosIcon color={"secondary"}/>
                    </ListItemIcon>
                    <ListItemText>
                      Step 1: Register your company and create projects easily.
                    </ListItemText>
                  </ListItem>
                  <ListItem>
                    <ListItemIcon>
                      <ArrowForwardIosIcon color={"secondary"}/>
                    </ListItemIcon>
                    <ListItemText>
                      Step 2: Give your employees and team members a chance to earn achievements with our rewards
                      system.
                    </ListItemText>
                  </ListItem>
                  <ListItem>
                    <ListItemIcon>
                      <ArrowForwardIosIcon color={"secondary"}/>
                    </ListItemIcon>
                    <ListItemText>
                      Step 3: PROFIT! (Through improved morale, raised productivity, and fun!)
                    </ListItemText>
                  </ListItem>
                </List>
              </StyledTypographySteps>
            </Collapse>
            <StyledTypographySub variant="body1">
              It's so easy!
            </StyledTypographySub>
            <ul>
              <li>Head over to our sign-up page</li>
              <li>Fill in some details about your company</li>
              <li>Add owners and admins</li>
              <li>Have your employees join</li>
              <li>Create rewards</li>
              <li>Reap the benefits: easily create and manage projects, set deadlines, calculate costs, assemble teams
                etc...
              </li>
            </ul>
            <StyledTypographySub variant="body1">
              Already a member?{' '}
              <StyledButton variant="contained" color="primary" component={Link} to="/login" autoComplete="off">
                Sign in here!
              </StyledButton>
              <br/>
              Still no account?{' '}
              <StyledButton variant="contained" color="primary" component={Link} to="/register" autoComplete="off">
                Let's get you started!
              </StyledButton>
            </StyledTypographySub>
          </StyledDiv>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Home;

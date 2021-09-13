import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomUser from './custom-user';
import CustomUserDetail from './custom-user-detail';
import CustomUserUpdate from './custom-user-update';
import CustomUserDeleteDialog from './custom-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomUserDeleteDialog} />
  </>
);

export default Routes;

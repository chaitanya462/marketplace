import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserEmail from './user-email';
import UserEmailDetail from './user-email-detail';
import UserEmailUpdate from './user-email-update';
import UserEmailDeleteDialog from './user-email-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserEmailUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserEmailUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserEmailDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserEmail} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserEmailDeleteDialog} />
  </>
);

export default Routes;

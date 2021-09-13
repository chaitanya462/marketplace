import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserPhone from './user-phone';
import UserPhoneDetail from './user-phone-detail';
import UserPhoneUpdate from './user-phone-update';
import UserPhoneDeleteDialog from './user-phone-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserPhoneUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserPhoneUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserPhoneDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserPhone} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserPhoneDeleteDialog} />
  </>
);

export default Routes;

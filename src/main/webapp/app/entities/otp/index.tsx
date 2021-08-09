import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Otp from './otp';
import OtpDetail from './otp-detail';
import OtpUpdate from './otp-update';
import OtpDeleteDialog from './otp-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OtpUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OtpUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OtpDetail} />
      <ErrorBoundaryRoute path={match.url} component={Otp} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OtpDeleteDialog} />
  </>
);

export default Routes;

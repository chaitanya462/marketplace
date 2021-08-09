import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OtpAttempt from './otp-attempt';
import OtpAttemptDetail from './otp-attempt-detail';
import OtpAttemptUpdate from './otp-attempt-update';
import OtpAttemptDeleteDialog from './otp-attempt-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OtpAttemptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OtpAttemptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OtpAttemptDetail} />
      <ErrorBoundaryRoute path={match.url} component={OtpAttempt} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OtpAttemptDeleteDialog} />
  </>
);

export default Routes;

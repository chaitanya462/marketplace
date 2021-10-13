import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VmsjobSubmit from './vmsjob-submit';
import VmsjobSubmitDetail from './vmsjob-submit-detail';
import VmsjobSubmitUpdate from './vmsjob-submit-update';
import VmsjobSubmitDeleteDialog from './vmsjob-submit-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VmsjobSubmitUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VmsjobSubmitUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VmsjobSubmitDetail} />
      <ErrorBoundaryRoute path={match.url} component={VmsjobSubmit} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VmsjobSubmitDeleteDialog} />
  </>
);

export default Routes;

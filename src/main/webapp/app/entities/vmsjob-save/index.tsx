import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VmsjobSave from './vmsjob-save';
import VmsjobSaveDetail from './vmsjob-save-detail';
import VmsjobSaveUpdate from './vmsjob-save-update';
import VmsjobSaveDeleteDialog from './vmsjob-save-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VmsjobSaveUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VmsjobSaveUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VmsjobSaveDetail} />
      <ErrorBoundaryRoute path={match.url} component={VmsjobSave} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VmsjobSaveDeleteDialog} />
  </>
);

export default Routes;

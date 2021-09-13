import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Refereces from './refereces';
import ReferecesDetail from './refereces-detail';
import ReferecesUpdate from './refereces-update';
import ReferecesDeleteDialog from './refereces-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReferecesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReferecesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReferecesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Refereces} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ReferecesDeleteDialog} />
  </>
);

export default Routes;

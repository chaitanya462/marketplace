import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubjectMaster from './subject-master';
import SubjectMasterDetail from './subject-master-detail';
import SubjectMasterUpdate from './subject-master-update';
import SubjectMasterDeleteDialog from './subject-master-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubjectMasterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubjectMasterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubjectMasterDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubjectMaster} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubjectMasterDeleteDialog} />
  </>
);

export default Routes;

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FieldValue from './field-value';
import FieldValueDetail from './field-value-detail';
import FieldValueUpdate from './field-value-update';
import FieldValueDeleteDialog from './field-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FieldValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FieldValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FieldValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={FieldValue} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FieldValueDeleteDialog} />
  </>
);

export default Routes;

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Certificate from './certificate';
import CertificateDetail from './certificate-detail';
import CertificateUpdate from './certificate-update';
import CertificateDeleteDialog from './certificate-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CertificateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CertificateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CertificateDetail} />
      <ErrorBoundaryRoute path={match.url} component={Certificate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CertificateDeleteDialog} />
  </>
);

export default Routes;

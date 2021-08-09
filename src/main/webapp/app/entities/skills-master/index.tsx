import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SkillsMaster from './skills-master';
import SkillsMasterDetail from './skills-master-detail';
import SkillsMasterUpdate from './skills-master-update';
import SkillsMasterDeleteDialog from './skills-master-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SkillsMasterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SkillsMasterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SkillsMasterDetail} />
      <ErrorBoundaryRoute path={match.url} component={SkillsMaster} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SkillsMasterDeleteDialog} />
  </>
);

export default Routes;

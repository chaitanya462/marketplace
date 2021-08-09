import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Otp from './otp';
import OtpAttempt from './otp-attempt';
import CustomUser from './custom-user';
import UserEmail from './user-email';
import UserPhone from './user-phone';
import Address from './address';
import Location from './location';
import Client from './client';
import Worker from './worker';
import File from './file';
import Education from './education';
import SubjectMaster from './subject-master';
import Certificate from './certificate';
import Employment from './employment';
import SkillsMaster from './skills-master';
import Portfolio from './portfolio';
import Refereces from './refereces';
import JobPreference from './job-preference';
import Category from './category';
import LocationPrefrence from './location-prefrence';
import Field from './field';
import FieldValue from './field-value';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}otp`} component={Otp} />
      <ErrorBoundaryRoute path={`${match.url}otp-attempt`} component={OtpAttempt} />
      <ErrorBoundaryRoute path={`${match.url}custom-user`} component={CustomUser} />
      <ErrorBoundaryRoute path={`${match.url}user-email`} component={UserEmail} />
      <ErrorBoundaryRoute path={`${match.url}user-phone`} component={UserPhone} />
      <ErrorBoundaryRoute path={`${match.url}address`} component={Address} />
      <ErrorBoundaryRoute path={`${match.url}location`} component={Location} />
      <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
      <ErrorBoundaryRoute path={`${match.url}worker`} component={Worker} />
      <ErrorBoundaryRoute path={`${match.url}file`} component={File} />
      <ErrorBoundaryRoute path={`${match.url}education`} component={Education} />
      <ErrorBoundaryRoute path={`${match.url}subject-master`} component={SubjectMaster} />
      <ErrorBoundaryRoute path={`${match.url}certificate`} component={Certificate} />
      <ErrorBoundaryRoute path={`${match.url}employment`} component={Employment} />
      <ErrorBoundaryRoute path={`${match.url}skills-master`} component={SkillsMaster} />
      <ErrorBoundaryRoute path={`${match.url}portfolio`} component={Portfolio} />
      <ErrorBoundaryRoute path={`${match.url}refereces`} component={Refereces} />
      <ErrorBoundaryRoute path={`${match.url}job-preference`} component={JobPreference} />
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}location-prefrence`} component={LocationPrefrence} />
      <ErrorBoundaryRoute path={`${match.url}field`} component={Field} />
      <ErrorBoundaryRoute path={`${match.url}field-value`} component={FieldValue} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

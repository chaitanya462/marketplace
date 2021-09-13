import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import otp from 'app/entities/otp/otp.reducer';
// prettier-ignore
import otpAttempt from 'app/entities/otp-attempt/otp-attempt.reducer';
// prettier-ignore
import customUser from 'app/entities/custom-user/custom-user.reducer';
// prettier-ignore
import userEmail from 'app/entities/user-email/user-email.reducer';
// prettier-ignore
import userPhone from 'app/entities/user-phone/user-phone.reducer';
// prettier-ignore
import address from 'app/entities/address/address.reducer';
// prettier-ignore
import location from 'app/entities/location/location.reducer';
// prettier-ignore
import client from 'app/entities/client/client.reducer';
// prettier-ignore
import worker from 'app/entities/worker/worker.reducer';
// prettier-ignore
import file from 'app/entities/file/file.reducer';
// prettier-ignore
import education from 'app/entities/education/education.reducer';
// prettier-ignore
import subjectMaster from 'app/entities/subject-master/subject-master.reducer';
// prettier-ignore
import certificate from 'app/entities/certificate/certificate.reducer';
// prettier-ignore
import employment from 'app/entities/employment/employment.reducer';
// prettier-ignore
import skillsMaster from 'app/entities/skills-master/skills-master.reducer';
// prettier-ignore
import portfolio from 'app/entities/portfolio/portfolio.reducer';
// prettier-ignore
import refereces from 'app/entities/refereces/refereces.reducer';
// prettier-ignore
import jobPreference from 'app/entities/job-preference/job-preference.reducer';
// prettier-ignore
import category from 'app/entities/category/category.reducer';
// prettier-ignore
import locationPrefrence from 'app/entities/location-prefrence/location-prefrence.reducer';
// prettier-ignore
import field from 'app/entities/field/field.reducer';
// prettier-ignore
import fieldValue from 'app/entities/field-value/field-value.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  otp,
  otpAttempt,
  customUser,
  userEmail,
  userPhone,
  address,
  location,
  client,
  worker,
  file,
  education,
  subjectMaster,
  certificate,
  employment,
  skillsMaster,
  portfolio,
  refereces,
  jobPreference,
  category,
  locationPrefrence,
  field,
  fieldValue,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;

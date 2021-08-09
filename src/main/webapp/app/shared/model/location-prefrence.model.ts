import { IJobPreference } from 'app/shared/model/job-preference.model';
import { ILocation } from 'app/shared/model/location.model';

export interface ILocationPrefrence {
  id?: number;
  prefrenceOrder?: number | null;
  worker?: IJobPreference | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<ILocationPrefrence> = {};

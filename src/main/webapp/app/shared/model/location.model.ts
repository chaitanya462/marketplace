import { ILocationPrefrence } from 'app/shared/model/location-prefrence.model';
import { IEmployment } from 'app/shared/model/employment.model';

export interface ILocation {
  id?: number;
  pincode?: number | null;
  country?: string | null;
  state?: string | null;
  city?: string | null;
  locationPrefrences?: ILocationPrefrence[] | null;
  employment?: IEmployment | null;
}

export const defaultValue: Readonly<ILocation> = {};

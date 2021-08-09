import { ILocation } from 'app/shared/model/location.model';
import { ICustomUser } from 'app/shared/model/custom-user.model';

export interface IAddress {
  id?: number;
  line1?: string | null;
  line2?: string | null;
  tag?: string | null;
  location?: ILocation | null;
  customUser?: ICustomUser | null;
}

export const defaultValue: Readonly<IAddress> = {};

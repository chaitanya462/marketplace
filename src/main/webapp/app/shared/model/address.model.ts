import { ILocation } from 'app/shared/model/location.model';
import { IUser } from 'app/shared/model/user.model';

export interface IAddress {
  id?: number;
  line1?: string | null;
  line2?: string | null;
  tag?: string | null;
  location?: ILocation | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IAddress> = {};

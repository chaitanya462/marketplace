import { IUserEmail } from 'app/shared/model/user-email.model';
import { IUserPhone } from 'app/shared/model/user-phone.model';
import { IAddress } from 'app/shared/model/address.model';

export interface ICustomUser {
  id?: number;
  userEmails?: IUserEmail[] | null;
  userPhones?: IUserPhone[] | null;
  addresses?: IAddress[] | null;
}

export const defaultValue: Readonly<ICustomUser> = {};

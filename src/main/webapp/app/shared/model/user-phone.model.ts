import { IUser } from 'app/shared/model/user.model';

export interface IUserPhone {
  id?: number;
  phone?: string | null;
  isActive?: boolean | null;
  isPrimary?: boolean | null;
  tag?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserPhone> = {
  isActive: false,
  isPrimary: false,
};

import { IUser } from 'app/shared/model/user.model';

export interface IUserEmail {
  id?: number;
  email?: string;
  isActive?: boolean | null;
  isPrimary?: boolean | null;
  tag?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserEmail> = {
  isActive: false,
  isPrimary: false,
};

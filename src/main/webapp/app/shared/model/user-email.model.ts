import dayjs from 'dayjs';
import { ICustomUser } from 'app/shared/model/custom-user.model';

export interface IUserEmail {
  id?: number;
  email?: string;
  isActive?: boolean | null;
  isPrimary?: boolean | null;
  tag?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  customUser?: ICustomUser | null;
}

export const defaultValue: Readonly<IUserEmail> = {
  isActive: false,
  isPrimary: false,
};

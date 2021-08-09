import dayjs from 'dayjs';
import { ICustomUser } from 'app/shared/model/custom-user.model';

export interface IUserPhone {
  id?: number;
  phone?: number;
  isActive?: boolean | null;
  isPrimary?: boolean | null;
  tag?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  customUser?: ICustomUser | null;
}

export const defaultValue: Readonly<IUserPhone> = {
  isActive: false,
  isPrimary: false,
};

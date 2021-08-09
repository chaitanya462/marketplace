import dayjs from 'dayjs';
import { IField } from 'app/shared/model/field.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  isParent?: boolean | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  categories?: ICategory[] | null;
  fields?: IField[] | null;
  parent?: ICategory | null;
}

export const defaultValue: Readonly<ICategory> = {
  isParent: false,
  isActive: false,
};

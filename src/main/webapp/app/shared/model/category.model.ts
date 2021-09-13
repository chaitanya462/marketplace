import { IField } from 'app/shared/model/field.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  isParent?: boolean | null;
  isActive?: boolean | null;
  categories?: ICategory[] | null;
  fields?: IField[] | null;
  parent?: ICategory | null;
}

export const defaultValue: Readonly<ICategory> = {
  isParent: false,
  isActive: false,
};

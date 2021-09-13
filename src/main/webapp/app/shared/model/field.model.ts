import { IFieldValue } from 'app/shared/model/field-value.model';
import { ICategory } from 'app/shared/model/category.model';
import { FieldType } from 'app/shared/model/enumerations/field-type.model';

export interface IField {
  id?: number;
  fieldName?: string | null;
  fieldLabel?: string | null;
  fieldType?: FieldType | null;
  isActive?: boolean | null;
  fieldValues?: IFieldValue[] | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IField> = {
  isActive: false,
};

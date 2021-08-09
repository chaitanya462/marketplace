import { IJobPreference } from 'app/shared/model/job-preference.model';
import { IField } from 'app/shared/model/field.model';

export interface IFieldValue {
  id?: number;
  value?: string | null;
  jobpreference?: IJobPreference | null;
  field?: IField | null;
}

export const defaultValue: Readonly<IFieldValue> = {};

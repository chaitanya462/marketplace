import dayjs from 'dayjs';
import { ICustomUser } from 'app/shared/model/custom-user.model';
import { CompanyType } from 'app/shared/model/enumerations/company-type.model';

export interface IClient {
  id?: number;
  companyName?: string | null;
  companyWebsite?: string | null;
  companyType?: CompanyType | null;
  primaryPhone?: number;
  description?: string | null;
  startDate?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  customUser?: ICustomUser | null;
}

export const defaultValue: Readonly<IClient> = {};

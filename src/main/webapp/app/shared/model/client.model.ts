import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { CompanyType } from 'app/shared/model/enumerations/company-type.model';

export interface IClient {
  id?: number;
  companyName?: string | null;
  companyWebsite?: string | null;
  companyType?: CompanyType | null;
  primaryPhone?: string | null;
  isActive?: boolean | null;
  description?: string | null;
  startDate?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IClient> = {
  isActive: false,
};

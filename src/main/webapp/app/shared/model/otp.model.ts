import dayjs from 'dayjs';
import { OtpType } from 'app/shared/model/enumerations/otp-type.model';
import { OtpStatus } from 'app/shared/model/enumerations/otp-status.model';

export interface IOtp {
  id?: number;
  contextId?: string | null;
  otp?: number | null;
  email?: string | null;
  isActive?: boolean | null;
  phone?: number | null;
  type?: OtpType | null;
  expiryTime?: string | null;
  status?: OtpStatus | null;
}

export const defaultValue: Readonly<IOtp> = {
  isActive: false,
};

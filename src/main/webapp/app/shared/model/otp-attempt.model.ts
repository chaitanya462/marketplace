import dayjs from 'dayjs';
import { OtpStatus } from 'app/shared/model/enumerations/otp-status.model';

export interface IOtpAttempt {
  id?: number;
  contextId?: string | null;
  otp?: number | null;
  status?: OtpStatus | null;
  ip?: string | null;
  coookie?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
}

export const defaultValue: Readonly<IOtpAttempt> = {};

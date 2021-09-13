import { OtpStatus } from 'app/shared/model/enumerations/otp-status.model';

export interface IOtpAttempt {
  id?: number;
  contextId?: string | null;
  otp?: number | null;
  isActive?: boolean | null;
  status?: OtpStatus | null;
  ip?: string | null;
  coookie?: string | null;
}

export const defaultValue: Readonly<IOtpAttempt> = {
  isActive: false,
};

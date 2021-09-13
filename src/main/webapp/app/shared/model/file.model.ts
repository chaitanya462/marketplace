import { IWorker } from 'app/shared/model/worker.model';
import { FileFormat } from 'app/shared/model/enumerations/file-format.model';
import { FileType } from 'app/shared/model/enumerations/file-type.model';

export interface IFile {
  id?: number;
  path?: string | null;
  fileformat?: FileFormat | null;
  filetype?: FileType | null;
  tag?: string | null;
  isDefault?: boolean | null;
  isResume?: boolean | null;
  isProfilePic?: boolean | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IFile> = {
  isDefault: false,
  isResume: false,
  isProfilePic: false,
};

import dayjs from 'dayjs';
import { ILocationPrefrence } from 'app/shared/model/location-prefrence.model';
import { IFieldValue } from 'app/shared/model/field-value.model';
import { ICategory } from 'app/shared/model/category.model';
import { IWorker } from 'app/shared/model/worker.model';
import { EngagementType } from 'app/shared/model/enumerations/engagement-type.model';
import { EmploymentType } from 'app/shared/model/enumerations/employment-type.model';
import { LocationType } from 'app/shared/model/enumerations/location-type.model';

export interface IJobPreference {
  id?: number;
  hourlyRate?: number | null;
  dailyRate?: number | null;
  monthlyRate?: number | null;
  hourPerDay?: number | null;
  hourPerWeek?: number | null;
  engagementType?: EngagementType | null;
  employmentType?: EmploymentType | null;
  locationType?: LocationType | null;
  availableFrom?: string | null;
  availableTo?: string | null;
  isActive?: boolean | null;
  locationPrefrences?: ILocationPrefrence[] | null;
  fieldValues?: IFieldValue[] | null;
  subCategory?: ICategory | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IJobPreference> = {
  isActive: false,
};

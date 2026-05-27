import { BASE_URLS, request } from './api';

export const enumService = {
  getAttendanceStatuses: (): Promise<string[]> =>
    request(`${BASE_URLS.main}/enums/attendance-statuses`),

  getGradeTypes: (): Promise<string[]> =>
    request(`${BASE_URLS.main}/enums/grade-types`),
};
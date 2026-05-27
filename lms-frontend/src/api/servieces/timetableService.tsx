import { BASE_URLS, request } from './api';
import type { Timetable, CreateTimetable, UpdateTimetable } from '../modules/timetable';

const BASE = `${BASE_URLS.main}/timetable`;

export const timetableService = {
  getById: (id: number): Promise<Timetable> =>
    request(BASE, {params: {id}}),

  getAll: (page: number = 1): Promise<{ data: Timetable[] }> =>
    request(`${BASE_URLS.main}/timetables/${page}`),

  getBySemester: (semesterId: number): Promise<Timetable[]> =>
    request(`${BASE_URLS.main}/timetables/by-semester/${semesterId}`),

  getByTeacher: (teacherId: number): Promise<Timetable[]> =>
    request(`${BASE_URLS.main}/timetables/by-teacher/${teacherId}`),

  getByGroup: (groupId: number): Promise<Timetable[]> =>
    request(`${BASE_URLS.main}/timetables/by-group/${groupId}`),

  create: (data: CreateTimetable): Promise<Timetable> =>
    request(BASE, {method: 'POST', body: JSON.stringify(data) }),

  update: (id: number, data: UpdateTimetable): Promise<Timetable> =>
    request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data) }),

  delete: (id: number): Promise<void> =>
    request(BASE, {method: 'DELETE', params: {id}}),
};
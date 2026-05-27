// import { request, BASE_URLS } from './api';

// export interface Mark {
//     id: number;
//     timetableId: number;
//     studentId: number;
//     lessonRealDate: string;
//     updatedAt: string;
//     attendanceStatus: string; // present/absent/late/excused
//     mark: number;
// }

// export const getMyMarks = () =>
//      request<Mark[]>(`${BASE_URLS.userGrade}/marks/me`);

// export const getMarksByDiscipline = (disciplineId: number) =>
//      request<Mark[]>(`${BASE_URLS.userGrade}/marks/${disciplineId}`);


// src/api/services/markService.ts
import { BASE_URLS, request } from './api';
import type { Mark, CreateMark, UpdateMark } from '../modules/mark';

const BASE = `${BASE_URLS.main}/mark`;

export const markService = {
    getById: (id: number): Promise<Mark> =>
        request(BASE, {params: {id }}),

    getAll: (page: number = 1): Promise<{data: Mark[]}> =>
        request(`${BASE_URLS.main}/marks/${page}`),

    getByStudent: (studentId: number): Promise<Mark[]> =>
        request(`${BASE_URLS.main}/marks/by-student/${studentId}`),

    getByTimetable: (timetableId: number): Promise<Mark[]> =>
        request(`${BASE_URLS.main}/marks/by-timetable/${timetableId}`),

    getByDateRange: (startDate: string, endDate: string): Promise<Mark[]> =>
        request(`${BASE_URLS.main}/marks/by-date/`, {params: {startDate, endDate}}),

    create: (data: CreateMark): Promise<Mark> =>
        request(BASE, {method: 'POST', body: JSON.stringify(data) }),

    update: (id: number, data: UpdateMark): Promise<Mark> =>
        request(BASE, { method: 'PUT', params: {id}, body: JSON.stringify(data)}),

    delete: (id: number): Promise<void> =>
        request(BASE, {method: 'DELETE', params: {id }}),
};
// import {request, BASE_URLS} from './api';

// export interface Lesson {
//     id: number;
//     disciplineId: number;
//     orderedNumber: number;
//     mainTheme: string;
//     description: string;
//     teacherFileLink: string;
//     studentsFileLink: string;
//     type: string;
//     format: string;
//     recommendRoom: string;
// }

// export const getLessonsByDiscipline = (disciplineId: number) =>
//   request<Lesson[]>(`${BASE_URLS.main}/lessons`, { params: { disciplineId } });


import { BASE_URLS, request } from './api';
import type { Lesson, CreateLesson, UpdateLesson } from '../modules/lesson';

const BASE = `${BASE_URLS.main}/lesson`;

export const lessonService = {
  getById: (id: number): Promise<Lesson> =>
    request(BASE, {params: {id}}),

  getAll: (page: number = 1): Promise<{ data: Lesson[] }> =>
    request(`${BASE_URLS.main}/lessons/${page}`),

  getByDiscipline: (disciplineId: number): Promise<Lesson[]> =>
    request(`${BASE_URLS.main}/lessons/by-discipline/${disciplineId}`),

  create: (data: CreateLesson): Promise<Lesson> =>
    request(BASE, {method: 'POST', body: JSON.stringify(data)}),

  update: (id: number, data: UpdateLesson): Promise<Lesson> =>
    request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data)}),

  delete: (id: number): Promise<void> =>
    request(BASE, {method: 'DELETE', params: {id}}),
};
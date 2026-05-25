// src/api/services/gradeService.ts
import { BASE_URLS, request } from './api.tsx';
import { type Grade, type CreateGradeDto, type UpdateGradeDto } from '../modules/grade.tsx';

const BASE = `${BASE_URLS.userGrade}/grade`;

export const gradeService = {
  getById: (id: number): Promise<Grade> =>
    request(BASE, { params: { id } }),

  getAll: (page: number = 1): Promise<{ data: Grade[] }> =>
    request(`${BASE_URLS.userGrade}/grades`, { params: { page } }),

  create: (data: CreateGradeDto): Promise<Grade> =>
    request(BASE, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  update: (id: number, data: UpdateGradeDto): Promise<Grade> =>
    request(BASE, {
      method: 'PUT',
      params: { id },
      body: JSON.stringify(data),
    }),

  delete: (id: number): Promise<void> =>
    request(BASE, { method: 'DELETE', params: { id } }),
};
// src/api/services/semesterService.ts
import { BASE_URLS, request } from './api.tsx';
import type { Semester, CreateSemesterDto, UpdateSemesterDto } from '../modules/semestr.tsx';

const BASE = `${BASE_URLS.main}/semester`;

export const semesterService = {
  getById: (id: number): Promise<Semester> =>
    request(BASE, { params: { id } }),

  getAll: (page: number = 1): Promise<{ data: Semester[] }> =>
    request(`${BASE_URLS.main}/semesters`, { params: { page } }),

  create: (data: CreateSemesterDto): Promise<Semester> =>
    request(BASE, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  update: (id: number, data: UpdateSemesterDto): Promise<Semester> =>
    request(BASE, {
      method: 'PUT',
      params: { id },
      body: JSON.stringify(data),
    }),

  delete: (id: number): Promise<void> =>
    request(BASE, { method: 'DELETE', params: { id } }),
};
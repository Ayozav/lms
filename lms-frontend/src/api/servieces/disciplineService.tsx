// src/api/services/disciplineService.ts
import { BASE_URLS, request } from './api';
import type { Discipline, CreateDiscipline, UpdateDiscipline } from '../modules/discipline';

const BASE = `${BASE_URLS.main}/discipline`;

export const disciplineService = {
  getById: (id: number): Promise<Discipline> =>
    request(BASE, {params: {id}}),

  getAll: (page: number = 1): Promise<{ data: Discipline[] }> =>
    request(`${BASE_URLS.main}/disciplines/${page}`),

  getBySupervisor: (supervisorId: number): Promise<Discipline[]> =>
    request(`${BASE_URLS.main}/disciplines/by-supervisor/${supervisorId}`),

  getBySemester: (semesterId: number): Promise<Discipline[]> =>
    request(`${BASE_URLS.main}/disciplines/by-semester/${semesterId}`),

  create: (data: CreateDiscipline): Promise<Discipline> =>
    request(BASE, {method: 'POST', body: JSON.stringify(data)}),

  update: (id: number, data: UpdateDiscipline): Promise<Discipline> =>
    request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data)}),

  delete: (id: number): Promise<void> =>
    request(BASE, {method: 'DELETE', params: {id}}),
};
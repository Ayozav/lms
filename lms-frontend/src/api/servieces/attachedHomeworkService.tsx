import { BASE_URLS, request } from './api';
import type { AttachedHomework, CreateAttachedHomework, UpdateAttachedHomework } from '../../pero/HomeworkInterfaces';

const BASE = `${BASE_URLS.main}/attached-homework`;

export const attachedHomeworkService = {
    getById: (id: number): Promise<AttachedHomework> =>
        request(BASE, {params: {id}}),

  getAll: (page: number = 1): Promise<{ data: AttachedHomework[] }> =>
    request(`${BASE_URLS.main}/attached-homeworks/${page}`),

  getByHomework: (homeworkId: number): Promise<AttachedHomework[]> =>
    request(`${BASE_URLS.main}/attached-homeworks/by-homework/${homeworkId}`),

  getByStudent: (studentId: number): Promise<AttachedHomework[]> =>
    request(`${BASE_URLS.main}/attached-homeworks/by-student/${studentId}`),

  create: (data: CreateAttachedHomework): Promise<AttachedHomework> =>
    request(BASE, {method: 'POST', body: JSON.stringify(data)}),

  update: (id: number, data: UpdateAttachedHomework): Promise<AttachedHomework> =>
    request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data)}),

  delete: (id: number): Promise<void> =>
    request(BASE, {method: 'DELETE', params: {id}}),

  grade: (id: number, mark: number): Promise<AttachedHomework> =>
    request(`${BASE}/grade`, {method: 'PATCH', params: {id, mark}}),
};
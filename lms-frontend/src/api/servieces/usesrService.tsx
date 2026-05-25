// src/api/services/userService.ts
import { BASE_URLS, request } from './api.tsx';
import type { User, CreateUserDto, UpdateUserDto } from '../modules/user';

const BASE = `${BASE_URLS.userGrade}/user`;

export const userService = {
  getById: (id: number): Promise<User> =>
    request(BASE, { params: { id } }),

  getAll: (page: number = 1): Promise<{ data: User[] }> =>
    request(`${BASE_URLS.userGrade}/users`, { params: { page } }),

  // Если бэкенд ожидает JSON в теле
  create: (data: CreateUserDto): Promise<User> =>
    request(BASE, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  // Если бэкенд ожидает query параметры (как в Yaak)
  createAsQuery: (data: CreateUserDto): Promise<User> =>
    request(BASE, {
      method: 'POST',
      params: data as any,
    }),

  update: (id: number, data: UpdateUserDto): Promise<User> =>
    request(BASE, {
      method: 'PUT',
      params: { id },
      body: JSON.stringify(data),
    }),

  delete: (id: number): Promise<void> =>
    request(BASE, { method: 'DELETE', params: { id } }),
};
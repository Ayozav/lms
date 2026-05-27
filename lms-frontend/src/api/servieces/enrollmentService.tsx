import { BASE_URLS, request } from './api';
import type {Enrollment, CreateEnrollment, UpdateEnrollment} from '../modules/enrollment';

const BASE = `${BASE_URLS.main}/enrollment`;

export const enrollmentService = {
    getById: (id: number): Promise<Enrollment> =>
        request(BASE, {params: {id}}), //?

    getAll: (page: number = 1): Promise<{ data: Enrollment[] }> =>
        request(`${BASE_URLS.main}/enrollments/${page}`),

    getByStudent: (studentId: number): Promise<Enrollment[]> =>
        request(`${BASE_URLS.main}/enrollments/by-student/${studentId}`),

    getByGroup: (groupId: number): Promise<Enrollment[]> =>
        request(`${BASE_URLS.main}/enrollments/by-group/${groupId}`),

    create: (data: CreateEnrollment): Promise<Enrollment> =>
        request(BASE, {method: 'POST', body: JSON.stringify(data)}),

    update: (id: number, data: UpdateEnrollment): Promise<Enrollment> =>
        request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data)}),

    delete: (id: number): Promise<void> =>
        request(BASE, {method: 'DELETE', params: {id}}),
};
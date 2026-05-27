import {BASE_URLS, request } from './api';
import type {Group, CreateGroup, UpdateGroup} from '../modules/group';

const BASE = `${BASE_URLS.main}/group`;

export const groupService = {
    getById: (id: number): Promise<Group> =>
        request(BASE, {params: {id}}),

    getAll: (page: number = 1): Promise<{ data: Group[] }> =>
        request(`${BASE_URLS.main}/groups`, {params: {page}}),

    getByGrade: (gradeId: number): Promise<Group[]> =>
        request(`${BASE_URLS.main}/groups/by-grade`, {params: {gradeId}}),

    getByHeadman: (headmanId: number): Promise<Group[]> =>
        request(`${BASE_URLS.main}/groups/by-headman`, {params: {headmanId}}),

    create: (data: CreateGroup): Promise<Group> =>
        request(BASE, {method: 'POST', body: JSON.stringify(data)}),

    update: (id: number, data: UpdateGroup): Promise<Group> =>
        request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data) }),

    delete: (id: number): Promise<void> =>
        request(BASE, {method: 'DELETE', params: {id}}),
};
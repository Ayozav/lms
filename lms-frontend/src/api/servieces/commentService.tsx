import { BASE_URLS, request } from './api';
import type { Comment, CreateComment, UpdateComment } from '../modules/comment';

const BASE = `${BASE_URLS.main}/comment`;

export const commentService = {
    getById: (id: number): Promise<Comment> =>
        request(BASE, {params: {id}}),

    getAll: (page: number = 1): Promise<{ data: Comment[] }> =>
        request(`${BASE_URLS.main}/comments/${page}`),

    getByAttachedHomework: (attachedHomeworkId: number): Promise<Comment[]> =>
        request(`${BASE_URLS.main}/comments/by-attached-homework/${attachedHomeworkId}`),

    create: (data: CreateComment): Promise<Comment> =>
        request(BASE, {method: 'POST', body: JSON.stringify(data)}),

    update: (id: number, data: UpdateComment): Promise<Comment> =>
        request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data)}),

    delete: (id: number): Promise<void> =>
        request(BASE, {method: 'DELETE', params: {id}}),
};
// import { request, BASE_URLS } from './api.tsx';


// СТАРАЯ РЕАЛИЗАЦИЯ
// //я не уверена, надо ли, но пусть будет??
// export interface Homework {
//   id: number;
//   lessonId: number;
//   semesterId: number;
//   deadline: string;
//   description: string;
//   fileLink: string;
// }

// export const getAllHomeworks = () => 
//   request<Homework[]>(`${BASE_URLS.main}/homeworks`);

// // БЕЗ понятия (сделаю позже...)??
// // ToDo: спросить об органзации отправки
// // export const submitHomework = () => 
// //     request<Homework[]>

// export const getHomeworkById = (id: number) => 
//   request<Homework>(`${BASE_URLS.main}/homeworks/${id}`);

// export const createHomework = (data: Omit<Homework, 'id'>) =>
//   request<Homework>(`${BASE_URLS.main}/homeworks`, {
//     method: 'POST',
//     body: JSON.stringify(data),
//   });


import { BASE_URLS, request } from './api';
import type { Homework, CreateHomework, UpdateHomework } from '../modules/homework';

const BASE = `${BASE_URLS.main}/homework`;

export const homeworkService = {
    getById: (id: number): Promise<Homework> =>
        request(BASE, {params: {id}}),

    getAll: (page: number = 1): Promise<{data: Homework[] }> =>
        request(`${BASE_URLS.main}/homeworks/${page}`),

    getByLesson: (lessonId: number): Promise<Homework[]> =>
        request(`${BASE_URLS.main}/homeworks/by-lesson/${lessonId}`),

    getBySemester: (semesterId: number): Promise<Homework[]> =>
        request(`${BASE_URLS.main}/homeworks/by-semester/${semesterId}`),

    create: (data: CreateHomework): Promise<Homework> =>
        request(BASE, {method: 'POST', body: JSON.stringify(data) }),

    update: (id: number, data: UpdateHomework): Promise<Homework> =>
        request(BASE, {method: 'PUT', params: {id}, body: JSON.stringify(data)}),

    delete: (id: number): Promise<void> =>
        request(BASE, {method: 'DELETE', params: {id}}),
};
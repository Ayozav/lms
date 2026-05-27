import { request, BASE_URLS } from './api.tsx';

import { type Note } from '../../pero/HomeworkInterfaces.tsx';

// `
// кавычки все руинили...

export const getNotes = () =>
    request<Note[]>(`${BASE_URLS.main}/notes/me`);

export const getNotesById = (id: number) => {
    request<Note[]>(`${BASE_URLS.main}/notes/${id}`)};
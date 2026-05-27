export interface Timetable {
  id: number;
  semesterId: number;
  disciplineId: number;
  teacherId: number;
  dayOfWeek: number;
  weekParity: number;
  room: string;
  startTime: string; // "HH:MM:SS"
  endTime: string;
}

export interface TimetableGroupLink {
  timetableId: number;
  groupId: number;
}

export type CreateTimetable = Omit<Timetable, 'id'>;
export type UpdateTimetable = Partial<CreateTimetable>;

export type CreateTimetableGroupLink = TimetableGroupLink; // композитный ключ
export type UpdateTimetableGroupLink = Partial<CreateTimetableGroupLink>;
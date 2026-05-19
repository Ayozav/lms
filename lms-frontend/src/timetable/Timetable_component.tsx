// структуры и прочее
export interface Subject_props {
  id: String;
  subject_name: String;
  teacher: String;
  classroom: String;
  is_appointed: Boolean;
  day: number;     // 0 понедельник 
  time: number;   
}

export interface TimetableProps {
  groupName: string;
  timetableData: Subject_props[];
  loading: boolean;
}

export const TIMESLOTS_kindergarten = [
  '09:00 - 10:35',
  '10:45 - 12:20',
  '13:20 - 14:55',
  '15:00 - 16:35',
  '16:40 - 18:15',
  '18:20 - 19:55',
];

export const TIMESLOTS_high = [
  '09:00 - 10:35',
  '10:45 - 12:20',
  '12:30 - 14:05',
  '15:00 - 16:35',
  '16:40 - 18:15',
  '18:20 - 19:55',
];

export const DAYS = ['Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'];

export const DEFAULT_TIMESLOTS = TIMESLOTS_kindergarten;
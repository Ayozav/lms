export interface Lesson {
  id: number;
  disciplineId: number;
  orderedNumber: number;
  mainTheme: string;
  description: string;
  teacherFileLink: string;
  studentsFileLink: string;
  type: string;
  format: string;
  recommendRoom: string;
}

export type CreateLesson = Omit<Lesson, 'id'>;
export type UpdateLesson = Partial<CreateLesson>;
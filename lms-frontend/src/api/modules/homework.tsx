export interface Homework {
  id: number;
  lessonId: number;
  semesterId: number;
  deadline: string;
  description: string;
  fileLink: string;
}

export type CreateHomework = Omit<Homework, 'id'>;
export type UpdateHomework = Partial<CreateHomework>;
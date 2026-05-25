export interface Semester {
  id: number;
  name: string;
  start: string;
  end: string;
}
export type CreateSemesterDto = Omit<Semester, 'id'>;
export type UpdateSemesterDto = Partial<CreateSemesterDto>;
export interface Enrollment {
  id: number;
  studentId: number;
  groupId: number;
  startSemesterId: number;
  endSemesterId: number | null;
}

export type CreateEnrollment = Omit<Enrollment, 'id'>;
export type UpdateEnrollment = Partial<CreateEnrollment>;
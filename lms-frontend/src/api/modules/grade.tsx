export type GradeType = 'BACHELOR' | 'MASTER' | 'SPECIALIST';
export interface Grade {
  id: number;
  code: string;
  grade_name: string;
  grade_type: GradeType;
  supervisor_id: number;
}
export type CreateGradeDto = Omit<Grade, 'id'>;
export type UpdateGradeDto = Partial<CreateGradeDto>;
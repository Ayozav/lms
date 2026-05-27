export type GradeTypes = 'BACHELOR' | 'MASTER' | 'SPECIALIST';

//snake_case
// export interface Grade {
//   id: number;
//   code: string;
//   grade_name: string;
//   grade_type: GradeType;
//   supervisor_id: number;
// }

//camelCase
export interface Grade {
  id: number;
  code: string;
  gradeName: String;
  gradeType: GradeTypes;
  supervisorId: number;
}

//ToDo: разобраться с @JsonProperty

export type CreateGradeDto = Omit<Grade, 'id'>;
export type UpdateGradeDto = Partial<CreateGradeDto>;
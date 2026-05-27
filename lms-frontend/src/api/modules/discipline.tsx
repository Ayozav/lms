export interface Discipline {
    id: number;
    disciplineName: string;
    supervisorId: number;
    description: string;
    semesterId: number;
    gradeId: number;
}

export type CreateDiscipline = Omit<Discipline, 'id'>;
export type UpdateDiscipline = Partial<CreateDiscipline>
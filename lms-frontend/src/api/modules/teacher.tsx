export interface TeachersAbility {
    teacherId: number;
    disciplineId: number;
}

export type CreateTeachersAbility = TeachersAbility; // композитный ключ, нет id
export type UpdateTeachersAbility = Partial<CreateTeachersAbility>;
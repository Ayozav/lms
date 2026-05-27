export interface Group {
    id: number;
    groupName: string;
    headmanId: number;
    firstSemesterId: number;
    courseLevel: number;
    gradeId: number;
}

export type CreateGroup = Omit<Group, 'id'>;
export type UpdateGroup = Partial<CreateGroup>;
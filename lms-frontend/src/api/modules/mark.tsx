export interface Mark {
    id: number;
    timetableId: number;
    studentId: number;
    lessonRealDate: string;
    updatedAt: string;
    attendanceStatus: string; // present/absent/late/excused
    mark: number;
}

export type CreateMark = Omit<Mark, 'id' | 'updatedAt'>;
export type UpdateMark = Partial<CreateMark>;
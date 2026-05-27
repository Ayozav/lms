export interface Homework {
  id: number;
  lessonId: number;
  semesterId: number;
  deadline: string; 
  description: string;
  fileLink: string;
}

export interface AttachedHomework {
  id: number;
  homeworkId: number;
  studentId: number;
  mark: number | null;
  attachDate: string;
}

export interface Comment {
  id: number;
  attachedHomeworkId: number;
  fromId: number;
  sendTime: string;
  message: string;
}

export interface Mark {
  id: number;
  timetableId: number;
  studentId: number;
  lessonRealDate: string;
  updatedAt: string;
  attendanceStatus: string; // 'present' / 'absent' / 'late' / 'excused'
  mark: number;
}

export interface Note {
  id: number;
  userId: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  tags?: string[];
}
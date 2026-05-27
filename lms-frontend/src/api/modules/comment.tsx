export interface Comment {
  id: number;
  attachedHomeworkId: number;
  fromId: number;
  sendTime: string; // ISO дата-время
  message: string;
}

export type CreateComment = Omit<Comment, 'id' | 'sendTime'>;
export type UpdateComment = Partial<CreateComment>;

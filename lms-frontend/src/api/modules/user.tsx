export interface User {
  id: number;
  firstName: string;
  lastName: string;
  patronymic: string;
  open_id: string;
  birthDate: string;
}
export type CreateUser = Omit<User, 'id'>;
export type UpdateUser = Partial<CreateUser>;
export interface User {
  id: number;
  first_name: string;
  last_name: string;
  patronymic?: string;
  open_id: string;
  birth_date: string;
}
export type CreateUserDto = Omit<User, 'id'>;
export type UpdateUserDto = Partial<CreateUserDto>;
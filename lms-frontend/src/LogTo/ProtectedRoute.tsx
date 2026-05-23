import { useLogto } from '@logto/react';
import type { ReactNode } from 'react';
import SignIn from './SignIn'; // импортируем МОЮ! кнопку

interface Props {
  children: ReactNode;
}

export default function ProtectedRoute({ children }: Props) {
  const { isAuthenticated } = useLogto();

  if (!isAuthenticated) {
    // кнопка входа?
    return <SignIn />;
  }

  return <>{children}</>;
}
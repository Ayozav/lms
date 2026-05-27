import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import MainPage from './mainPage/MainPage.tsx';


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* <App /> */}
    <MainPage />
  </StrictMode>,
);
// Страница, которая встретит всяк туда входящего.
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import Welcome_page from './Welcome_page.tsx';


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Welcome_page />
  </StrictMode>,
);
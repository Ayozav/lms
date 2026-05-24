import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import './App_styles.css'
import App from './App.tsx'

import Main_page from './main_page/Main_page.tsx'


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* <App /> */}
    
    {/* ??? */}
    <Main_page />
  </StrictMode>,
);
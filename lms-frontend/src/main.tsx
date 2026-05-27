import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

// import MainPage from './mainPage/MainPage.tsx';
import Welcome_page from './Welcome_page.tsx';


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* <App /> */}
    {/* <MainPage /> */}
    <Welcome_page />
  </StrictMode>,
);
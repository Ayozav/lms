import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
// import './index.css'
// import './App_styles.css'
// import App from './App.tsx'


import Main_page from './main_page/Main_page.tsx'
// import Timetable from './timetable/Timetable.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* <App /> */}
    {/* <Timetable /> -А как вы узнали, что файлы пишутся с Upper-case буквы? -Больно. */}
    {<Main_page />}
  </StrictMode>,
)

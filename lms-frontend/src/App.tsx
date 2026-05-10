import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import './App_styles.css'; 

function App() {
  return (
    <div>
      <Button variant="contained" size="medium" color="primary">Привет я кнопка</Button>
      <Button variant="outlined" color="secondary" size="large">Привет я тоже кнопка, но с подвохом</Button> 
      <Button variant="contained" className='im-still-want-custom-button'>А я изгой, зато красивый</Button>
'

      <TextField variant="filled" label="Введите хоть что-то"/>
    </div>
  );
}

export default App;
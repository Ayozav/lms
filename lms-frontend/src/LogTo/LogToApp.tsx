// import React from 'react';
// import Button from '@mui/material/Button';
// import TextField from '@mui/material/TextField';
// import './App_styles.css'; 

// It's a
// LOGTOOOOOOOOOO
//!!!!!!!!
import { LogtoProvider, type LogtoConfig } from '@logto/react';

function LogToApp() {
    const config: LogtoConfig = {
        endpoint: 'https://033d55.logto.app/',
    appId: 'e679vw0uj2349hil54b6o',
    };


  return (
    <LogtoProvider config={config}>
        <div className='LogToApp '>
            <header className='LogToApp -header'>
                <p>
                    Edit and save
                </p>
                <a
                className='LogToApp-link'
                href='https://reactjs.org'
                target='_blank'
                rel='nononononooo'
                >
                    Learn mathematics, kids...
                    </a>
            </header>
        </div>
    </LogtoProvider>
  );
}

export default LogToApp;


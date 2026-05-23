// import React from 'react';
import './App_styles.css'; 

// logto...
import { LogtoProvider, type LogtoConfig } from '@logto/react';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Callback from './LogTo/Callback';
import Home from './LogTo/Home';


function App() {
    const config: LogtoConfig = {
        endpoint: 'https://033d55.logto.app/',
        appId: 'e679vw0uj2349hil54b6o',
    };

    return (
        <BrowserRouter>
            <LogtoProvider config={config}>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/callback" element={<Callback />} />
                </Routes>
            </LogtoProvider>
        </BrowserRouter>
    );
}

export default App;
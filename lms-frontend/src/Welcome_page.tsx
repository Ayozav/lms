import { useState, useEffect } from 'react';
import {BrowserRouter, Routes, Route } from 'react-router-dom';

import iateLogo from './assets/welkome/iate_logo.svg';
import { Box, Typography, Button, CardMedia, Stack } from '@mui/material';

const imageModules = import.meta.glob('./assets/welkome/Frame*.png', { eager: true }) as Record<string, { default: string }>;
const photos = Object.values(imageModules).map(m => m.default);


function WelcomePageContent() {

    // Линки-переходы в и из системы
    const handleLogin = () => { window.open('/mainpage', '_blank'); };
    const handleLmsMore = () => { window.open('https://github.com/Ayozav/lms', '_blank'); };


    // Пикчи
    const [currentPhoto, setCurrentPhoto] = useState(0);
    useEffect(
        () => {
            const interval = setInterval(() => {
                setCurrentPhoto((prev) => (prev + 1) % photos.length);
            }, 15000);
            
            return () => clearInterval(interval);
    }, []);


    return <Box>
        {/* верхняя панель */}
        <Box sx={{ background: '#747DCF', borderRadius: 5, mb: 3 }}>
            <Box sx={{width: "95%", height: 120, display: 'flex', alignItems: 'center', pl: 5,}} >
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 5 }}>
                    <img src={iateLogo} alt="IATE Logo" style={{ height: 60 }} />
                    <Typography variant="h3" noWrap sx={{ fontFamily: 'Plaster', color: '#ffffff' }}>LMS</Typography>
                </Box>
                <Box sx={{ 
                        display: { 
                            xs: 'none',
                            sm: 'none',
                            md: 'flex', // показать от md
                            lg: 'flex',
                            xl: 'flex'
                        }, 
                        gap: 2, 
                        marginLeft: "auto", 
                        width: "max"
                        }}>
                    <Button variant='outlined' onClick={handleLmsMore} sx={{width: 220, height: 50, borderRadius: 5, borderColor: 'white', color: 'white', bgcolor: 'rgba(255, 255, 255, 0.5)', '&:hover': { bgcolor: '#5F66AC' },}}>
                        Узнать больше об LMS
                    </Button>
                    <Button variant='outlined'onClick={handleLogin} sx={{ borderColor: 'white', width: 110, height: 50, borderRadius: 5, color: 'white', bgcolor: 'rgba(255, 255, 255, 0.5)','&:hover': { bgcolor: '#5F66AC' },}}>
                        Войти
                    </Button>
                </Box>
            </Box>
        </Box>

            {/* основное */}
            <Box sx={{ p: 5, background: '#eeeffc', borderRadius: 5 }}>
                <Box>
                    <Typography variant="h4" sx={{ fontFamily: 'Jura', gap: 2, mb: 2, color: 'ActiveBorder' }}>
                        LMS «Деятель»
                    </Typography>
                    <Typography variant="h6" sx={{ fontFamily: 'Jura', gap: 2, mb: 2 }}>
                        Система управления обучением студентов ИАТЭ НИЯУ МИФИ
                    </Typography>

                    <CardMedia component="img" image={photos[currentPhoto]} sx={{ height: 500, borderRadius: 5 }} />
                    <Stack direction="row" spacing={2} sx={{ mt: 2, justifyContent: 'center' }}></Stack>
                </Box>
            </Box>
        </Box>
    ;
}

    // роут пути
    function WelcomePage() {
    return (
        <BrowserRouter>
        <Routes>
            <Route path="/" element={<WelcomePageContent />} />
            {/* <Route path="https://github.com/Ayozav/lms" /> */}
        </Routes>
        </BrowserRouter>
    );
}

export default WelcomePage;
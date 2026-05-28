import { useState, useEffect } from 'react';
import {BrowserRouter, Routes, Route } from 'react-router-dom';

import iateLogo from './assets/welkome/iate_logo.svg';
import { Box, Typography, Button, CardMedia, Stack } from '@mui/material';
import MainPage from './mainPage/MainPage';

const imageModules = import.meta.glob('./assets/welkome/Frame*.png', { eager: true }) as Record<string, { default: string }>;
const photos = Object.values(imageModules).map(m => m.default);


function WelcomePageContent() {
    // const navigate = useNavigate();

    const handleLogin = () => {
        // navigate('/mainpage');
        window.open('/mainpage', '_blank');
    };

    const handleLmsMore = () => {
            window.open('https://github.com/Ayozav/lms', '_blank');
    };


    //пикчи
    const [currentPhoto, setCurrentPhoto] = useState(0);

    useEffect(() => {
        const interval = setInterval(() => {
        setCurrentPhoto((prev) => (prev + 1) % photos.length);
        }, 15000);
        return () => clearInterval(interval);
    }, []);

    // const handlePrev = () => {
    //   setCurrentPhoto((prev) => (prev === 0 ? photos.length - 1 : prev - 1));
    // };

    // const handleNext = () => {
    //   setCurrentPhoto((prev) => (prev + 1) % photos.length);
    // };

        return (
            <Box>
            {/* верхняя панель */}
            <Box sx={{ background: '#747DCF', borderRadius: 5, mb: 3 }}>
                <Box sx={{width: 1440, height: 120, display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 5,}} >
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 5 }}>
                        <img src={iateLogo} alt="IATE Logo" style={{ height: 60 }} />

                        <Typography variant="h3" noWrap sx={{ fontFamily: 'Plaster', color: '#ffffff' }}>
                            LMS
                        </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2 }}>
                        <Button variant='outlined' onClick={handleLmsMore} sx={{width: 220, height: 50, borderRadius: 5, borderColor: 'white', color: 'white', bgcolor: 'rgba(255, 255, 255, 0.5)', '&:hover': { bgcolor: '#5F66AC' },}}>
                            Узнать больше об LMS
                        </Button>
                        <Button variant='outlined'onClick={handleLogin} sx={{ borderColor: 'white',width: 110, height: 50, borderRadius: 5, color: 'white', bgcolor: 'rgba(255, 255, 255, 0.5)','&:hover': { bgcolor: '#5F66AC' },}}>
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
        );
    }

    // роут пути
    function WelcomePage() {
    return (
        <BrowserRouter>
        <Routes>
            <Route path="/" element={<WelcomePageContent />} />
            <Route path="/mainpage" element={<MainPage />} />
            {/* <Route path="https://github.com/Ayozav/lms" /> */}
        </Routes>
        </BrowserRouter>
    );
}

export default WelcomePage;
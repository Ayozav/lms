import { Box, Button, CardMedia, Stack } from '@mui/material';
import { useState } from 'react';

//я хз как это оптимизировать...
const photos = [
    './assets/welkome/Frame 20.png',
    './assets/welkome/Frame 21.png',
    './assets/welkome/Frame 22.png',
    './assets/welkome/Frame 23.png',
    './assets/welkome/Frame 24.png',
    './assets/welkome/Frame 25.png',
    './assets/welkome/Frame 26.png',
    './assets/welkome/Frame 27.png',
    './assets/welkome/Frame 28.png',
    './assets/welkome/Frame 29.png',
    './assets/welkome/Frame 30.png',
    './assets/welkome/Frame 31.png',
    './assets/welkome/Frame 32.png',
    './assets/welkome/Frame 33.png',
    './assets/welkome/Frame 34.png',
]

export const Welcome = () => {
    const [currentPhoto, setCurrentPhoto] = useState(0);
    
    return (
        <Box>
            <CardMedia component="img" image={photos[currentPhoto]} sx={{ height: 300 }} />
            
            <Stack direction="row" spacing={2} sx={{ mt: 2, justifyContent: 'center' }}>
                <Button variant="contained" onClick={() => setCurrentPhoto(prev => prev - 1)} disabled={currentPhoto === 0}>
                    Назад
                </Button>
                <Button variant="contained" onClick={() => setCurrentPhoto(prev => prev + 1)} disabled={currentPhoto === photos.length - 1}>
                    Вперед
                </Button>
            </Stack>
        </Box>
    );
};
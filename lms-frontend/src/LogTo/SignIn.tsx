import { useLogto } from '@logto/react';
import { Button, Box, Paper } from '@mui/material';
import './SignIn.css';

const SignIn = () => {
    const { signIn, isAuthenticated } = useLogto();

    if (isAuthenticated) {
        return <div>u are signed in</div>;
    }

    return (
        <Box
            sx={{
                backgroundImage: 'url("../assets/bg_blured.png")',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
                minHeight: '100vh',
                width: '100%',
            }}
        >

        <Box 
            className="bg_pic"
            //а я хуй знает как сделать ее широкую во все окно
            sx={{display:'cover'}}> 
            <Box
                sx={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100vh',
                    width: '100%',
                }}
            >
                <Paper
                    elevation={2} 
                    sx={{
                        borderRadius: '30px', 
                        padding: '60px',     
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        backgroundColor: 'rgba(255, 255, 255, 0.25)',
                    }}
                >
                    <Button 
                        variant="contained" 
                        sx={{ borderRadius: '15px'}} 
                        color="secondary" //и как это переопределить в здравом уме...
                        onClick={() => signIn('http://localhost:5173/callback')}
                    >
                        Войти в LMS систему
                    </Button>
                </Paper>
            </Box>
        </Box>    
        </Box>
    );
}

export default SignIn;
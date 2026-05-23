import {useLogto} from '@logto/react';
import { Button } from '@mui/material';


const SignIn = () => {
    const {signIn, isAuthenticated} = useLogto();

    if (isAuthenticated) {
        return <div>u are signed in</div>;
    }

    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh",
                width: "100%",
            }} >
            <Button variant="contained" color="primary" onClick={() => signIn('http://localhost:3000/callback')}>
                Войти в LMS систему
            </Button>
        </div>
    );
}

export default SignIn;

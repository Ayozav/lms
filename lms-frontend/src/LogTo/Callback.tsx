import { useHandleSignInCallback } from '@logto/react';
import { useNavigate } from 'react-router';

const Callback = () => {
    const navigate = useNavigate();
    const { isLoading } = useHandleSignInCallback(() => {
        navigate('/'); //djpdhfoftvcz yf ukfdye. cnhfybwe
    });
    
    if (isLoading) {
        return <div>Redirecting...</div>;
    }
    return null;
};

export default Callback;
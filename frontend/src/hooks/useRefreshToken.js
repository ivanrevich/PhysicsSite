import axios from '../api/axios';
import useAuth from './useAuth';

const useRefreshToken = () => {
    const {auth, setAuth } = useAuth();

    const refresh = async () => {
            const response = await axios.post('/api/v1/auth/refresh-token', {}, {
                withCredentials: false,
                headers: {
                    'Authorization':`Bearer ${auth?.refreshToken}`,
                    'Content-Type': 'application/json',
                }
            });
            setAuth(prev => {
                return { ...prev, accessToken: response.data.access_token }
            });
            console.log(auth.accessToken)
            return response.data.access_token;
    }
    return refresh;
};

export default useRefreshToken;
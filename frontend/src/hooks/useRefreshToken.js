import axios from '../api/axios';
import useAuth from './useAuth';

const useRefreshToken = () => {
    const {auth, setAuth } = useAuth();

    const refresh = async () => {
        console.log(`Bearer ${auth?.refreshToken}`)
        const response = await axios.post('/api/v1/auth/refresh-token', {}, {
            //withCredentials: true,
            headers: {
                'Authorization':`Bearer ${auth?.refreshToken}`,
                'Content-Type': 'application/json',
            }
        });
        setAuth(prev => {
            return { ...prev, accessToken: response.data.access_token }
        });
        return response.data.accessToken;
    }
    return refresh;
};

export default useRefreshToken;
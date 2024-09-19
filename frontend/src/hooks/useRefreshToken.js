import axios from '../api/axios';
import useAuth from './useAuth';

const useRefreshToken = () => {
    const { setAuth } = useAuth();

    const refresh = async (accessToken) => {
        const response = await axios.post('/api/v1/auth/refresh-token', {}, {
            //withCredentials: true,
            headers: {
                'Authorization':`${accessToken}`,
                'Content-Type': 'application/json',
            }
        });
        console.log(response.data)
        setAuth(prev => {
            return { ...prev, accessToken: response.data.accessToken }
        });
        return response.data.accessToken;
    }
    return refresh;
};

export default useRefreshToken;
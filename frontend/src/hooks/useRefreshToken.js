import axios from '../api/axios';
import useAuth from './useAuth';

const useRefreshToken = () => {
    const {auth, setAuth } = useAuth();

    const refresh = async () => {
        try{
            console.log(`Bearer ${auth?.refreshToken}`)
            const response = await axios.post('/api/v1/auth/refresh-token', {}, {
                withCredentials: false,
                headers: {
                    'Authorization':`Bearer ${auth?.refreshToken}`,
                    'Content-Type': 'application/json',
                }
            });
            console.log(response);
            console.log(response.data);
            setAuth(prev => {
                return { ...prev, accessToken: response.data.access_token }
            });
            console.log(auth.accessToken)
            return response.data.accessToken;
        }catch(e){
            return auth?.accessToken
        }
    }
    return refresh;
};

export default useRefreshToken;
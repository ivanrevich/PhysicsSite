import axios from '../api/axios';
import useAuth from './useAuth';
import useAxiosPrivate from './useAxiosPrivate';

const useRefreshToken = () => {
    const { setAuth } = useAuth();
    const axiosPrivate = useAxiosPrivate()

    const refresh = async () => {
        const response = await axiosPrivate.post('/api/v1/auth/refresh-token');
        console.log(response.data)
        setAuth(prev => {
            return { ...prev, accessToken: response.data.accessToken }
        });
        return response.data.accessToken;
    }
    return refresh;
};

export default useRefreshToken;
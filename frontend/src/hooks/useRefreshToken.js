import axios from '../api/axios';
import useAuth from './useAuth';

const useRefreshToken = () => {
    const { auth, setAuth } = useAuth(); 
    const [isRefreshing, setIsRefreshing] = useState(false); 
    const [refreshError, setRefreshError] = useState(null);

    const refresh = async () => { 
        // Если запрос на обновление уже выполняется, просто выходим из функции.
        if (isRefreshing) {
            return;
        }

        setIsRefreshing(true); // Устанавливаем флаг, что запрос только начался.
        setRefreshError(null); // Сбрасываем предыдущую ошибку.

        try {
            console.log(`Bearer ${auth?.refreshToken}`); 
            const response = await axios.post('/api/v1/auth/refresh-token', {}, { 
                withCredentials: false, 
                headers: { 
                    'Authorization': `Bearer ${auth?.refreshToken}`, 
                    'Content-Type': 'application/json', 
                } 
            }); 

            setAuth(prev => ({ 
                ...prev, 
                accessToken: response.data.access_token 
            })); 

            console.log(response.data.access_token); 
            return response.data.access_token; 

        } catch (error) {
            console.error('Error refreshing token:', error);
            setRefreshError(error); // Сохраняем ошибку в состоянии, чтобы вы могли ее обработать.
            throw error; // Перебрасываем ошибку дальше, если это необходимо.
        } finally {
            setIsRefreshing(false); // В конце сбрасываем флаг, чтобы разрешить новые запросы.
        }
    };

    return { refresh, refreshError }; 
}; 

export default useRefreshToken;
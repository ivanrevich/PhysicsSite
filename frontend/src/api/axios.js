import axios from 'axios';
const BASE_URL = 'http://127.0.0.5:8080';

const axiosInstance = axios.create({
    baseURL: BASE_URL,
});

// Создаем защищенный экземпляр Axios
export const axiosPrivate = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});
export default axiosInstance;
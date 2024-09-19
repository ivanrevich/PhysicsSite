import axios from 'axios';
const BASE_URL = 'http://physics-site-816529-b1036c-77-73-132-179.traefik.me';

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
import axiosPrivate from "../api/axios"; 
import { useEffect, useState } from "react"; 
import useRefreshToken from "./useRefreshToken"; 
import useAuth from "./useAuth"; 

const useAxiosPrivate = () => { 
    const refresh = useRefreshToken(); 
    const { auth } = useAuth(); 
    const [isRefreshing, setIsRefreshing] = useState(false); 
    const [pendingRequests, setPendingRequests] = useState([]); 

    useEffect(() => { 
        const requestIntercept = axiosPrivate.interceptors.request.use( 
            config => { 
                if (!config.headers['Authorization']) { 
                    config.headers['Authorization'] = `Bearer ${auth?.accessToken}`; 
                } 
                return config; 
            }, (error) => Promise.reject(error) 
        ); 

        const responseIntercept = axiosPrivate.interceptors.response.use( 
            response => response, 
            async (error) => { 
                const prevRequest = error?.config; 

                if (error?.response?.status === 403 && !prevRequest?.sent) { 
                    if (!isRefreshing) { 
                        setIsRefreshing(true); 
                        prevRequest.sent = true; 

                        // Обновляем токен и обрабатываем ожидания
                        try {
                            const newAccessToken = await refresh(); 
                            
                            // Обновляем все ожидающие запросы с новым токеном
                            pendingRequests.forEach(callback => callback(newAccessToken)); 

                            // После успешного обновления токена обрабатываем текущий запрос
                            prevRequest.headers['Authorization'] = `Bearer ${newAccessToken}`; 
                            return axiosPrivate(prevRequest); 
                        } catch (err) {
                            // Обрабатываем ошибки обновления токена
                            return Promise.reject(err);
                        } finally {
                            setIsRefreshing(false);
                            setPendingRequests([]); // Сбрасываем ожидающие запросы
                        }
                    } else {
                        // Если уже идет процесс обновления, добавляем текущий запрос в очередь
                        return new Promise((resolve, reject) => {
                            setPendingRequests(prev => 
                                [...prev, (newAccessToken) => {
                                    prevRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
                                    resolve(axiosPrivate(prevRequest)); 
                                }]
                            );
                        });
                    }
                }
                
                return Promise.reject(error); 
            } 
        ); 

        return () => { 
            axiosPrivate.interceptors.request.eject(requestIntercept); 
            axiosPrivate.interceptors.response.eject(responseIntercept); 
        }; 
    }, [auth, refresh, pendingRequests, isRefreshing]); 

    return axiosPrivate; 
}; 

export default useAxiosPrivate;

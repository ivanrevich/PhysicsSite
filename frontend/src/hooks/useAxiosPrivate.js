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

                        try {
                            const newAccessToken = await refresh(); 

                            // Обновляем заголовок авторизации для текущего запроса
                            prevRequest.headers['Authorization'] = `Bearer ${newAccessToken}`; 

                            // Выполняем запрос заново с новым токеном
                            return axiosPrivate(prevRequest); 
                        } catch (err) {
                            return Promise.reject(err);
                        } finally {
                            setIsRefreshing(false);
                        }
                    } else {
                        // Если уже идет процесс обновления, добавляем текущий запрос в очередь
                        return new Promise((resolve, reject) => {
                            const requestWithNewToken = () => {
                                prevRequest.headers['Authorization'] = `Bearer ${auth?.accessToken}`; 
                                resolve(axiosPrivate(prevRequest)); 
                            };
                            setPendingRequests(prev => [...prev, requestWithNewToken]);
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
    }, [auth, refresh, isRefreshing]); 

    return axiosPrivate; 
}; 

export default useAxiosPrivate;

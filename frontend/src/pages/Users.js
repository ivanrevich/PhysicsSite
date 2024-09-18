import { useState, useEffect } from "react";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { useLocation } from "react-router-dom";
import { Avatar, Box, List, ListItem, ListItemContent, ListItemDecorator, Typography } from "@mui/joy";


const Users = ({ navigate }) => {
    const axiosPrivate = useAxiosPrivate();
    const [users, setUsers] = useState();
    const location = useLocation();

    useEffect(() => {
        let isMounted = true;
        const controller = new AbortController();
        const getUsers = async () => {
            try {
                const response = await axiosPrivate.get('/api/v1/users/getAll',
                    {
                        params: {
                            page: 1,
                        },
                    });
                isMounted && setUsers(response.data);
            } catch (err) {
                console.error(err);
                navigate('/login', { state: { from: location }, replace: true });
            }
        }

        getUsers();

        return () => {
            isMounted = false;
            controller.abort();
        }
    }, [])

    return (<Box sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
        flexGrow: 1,
    }}>
        {users?.length
            ? (
                <List aria-labelledby="ellipsis-list-demo"
                    sx={{ '--ListItemDecorator-size': '56px' }}>
                    {users.map((user, i) =>
                        <ListItem key={i}>
                            <ListItemDecorator>
                                <Avatar />
                            </ListItemDecorator>
                            <ListItemContent>
                                <Typography level="title-sm">{user?.name} {user?.surname}</Typography>
                                <Typography level="body-sm" noWrap>
                                    {user?.email}
                                </Typography>
                            </ListItemContent>
                        </ListItem>
                    )}
                </List>
            ) : <p>Нет пользователей</p>
        }
    </Box>)
};

export default Users;
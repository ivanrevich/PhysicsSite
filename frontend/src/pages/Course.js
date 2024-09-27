import { Divider, IconButton, Input, Sheet, Stack, Stepper, Typography } from "@mui/joy"
import LevelComponent from "../components/LevelComponent"
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { useEffect, useState } from "react";
import { AddCircle } from "@mui/icons-material";
import useWindowDimensions from "../hooks/useWindowDimentions";

const CoursePage = ({ navigate, getLessonEdit, setTitle, setIndex}) => {
    const axiosPrivate = useAxiosPrivate();
    const [lvlTitle, setLvlTitle] = useState('');
    const [lvls, setLvls] = useState();
    const [addBtnLoading, setAddBtnLoading] = useState(false);
    const { width, height } = useWindowDimensions();

    const getLvls = async () => {
        try {
            const response = await axiosPrivate.get('/api/v1/lvl');
            const a = response.data.sort((a, b) => (a.stepId > b.stepId) ? 1 : -1);
            a.map((el) => {
                el.lessons=el.lessons.sort((a, b) => (a.stepId > b.stepId) ? 1 : -1);
            });
            setLvls(a);
        } catch (err) {
            console.error(err);
            navigate('/login');
        }
    }

    useEffect(() => {
        const controller = new AbortController();

        getLvls();

        return () => {
            controller.abort();
        }
    }, [])

    const handleAddingLvl = async () => {
        setAddBtnLoading(true)
        try {
            const response = await axiosPrivate.post("/api/v1/lvl",
                JSON.stringify({ title: lvlTitle }),
                {
                    headers: { 'Content-Type': 'application/json' },
                    // withCredentials: true
                }
            );
            setLvlTitle("");
            if (response.data != null) {
                setLvls([...lvls, response.data])
            }
        } finally {
            setAddBtnLoading(false)
        }
    }

    const handleDeleteLvl = async (level) => {
        await axiosPrivate.delete("/api/v1/lvl/delete",
            {
                params: {
                    id: level.id,
                },
            },
            {
                headers: { 
                    'Content-Type': 'application/json', 
                } 
            }
        )
        setLvls(
            lvls.filter(el =>
                el.id !== level.id
            )
        )
        setIndex(-1);
        setTitle("");
    }

    const handleDeleteLesson = async (lesson_id) => {
        await axiosPrivate.delete("/api/v1/lessons/delete",
            {
                params: {
                    id: lesson_id,
                },
            },
            {
                headers: { 
                    'Content-Type': 'application/json', 
                } 
            }
        )
        const out = lvls.map(el => {
            el.lessons = el.lessons.filter(l =>
                l.id !== lesson_id
            );
            return el;
        });
        setLvls(out);
        setIndex(-1);
        setTitle("");
    }

    const handleAddingLesson = async (level, newLessonName) => {
        const response = await axiosPrivate.post("/api/v1/lessons/create",
            JSON.stringify({ title: newLessonName, levelid: level.id }),
            {
                headers: { 'Content-Type': 'application/json' },
                // withCredentials: true
            }
        )
        const out = lvls.map(el => {
            if (el.id === level.id && response.data != null) {
                el.lessons.push(response?.data);
            }
            return el;
        }
        )
        setLvls(out);
    }


    return (<Sheet variant="outlined" sx={{
        borderRadius: 5,
        display: "flex",
        minWidth: 400,
        width: (25 * (width / 100)),
        justifyContent: "top",
        alignItems: "center",
        flexDirection: "column",
        overflowY:"scroll",
        scrollbarWidth:"none",
        pb:2
    }}>
        <Typography
            level="body-xs"
            textTransform="uppercase"
            alignSelf={"flex-start"}
            paddingTop={2}
            paddingLeft={2}
            sx={{ letterSpacing: '0.15rem' }}
        >
            Обзор курса
        </Typography>
        <Divider sx={{ mt: 2 }}></Divider>
        {lvls?.length
            ? (
                <Stepper orientation="vertical" sx={{ px: 2, py: 3 }} size="lg">
                    {lvls.map((lvl, i) =>
                        <LevelComponent level={lvl} ix={i} getLessonEdit={getLessonEdit} handleAddingLesson={handleAddingLesson} handleDeleteLesson={handleDeleteLesson} handleDeleteLvl={() => handleDeleteLvl(lvl)} />
                    )}
                </Stepper>
            ) : <p>Пока что нет уровней</p>
        }
        <Stack direction="row" gap={1} sx={{ py: 1, flexBasis:1,}}>
            <Input variant="outlined" placeholder="Добавить уровень" value={lvlTitle} onChange={(e) => setLvlTitle(e.target.value)} sx={{ maxWidth: 300, flexGrow: 1, display: "flex" }} />
            <IconButton loading={addBtnLoading} onClick={() => handleAddingLvl()}><AddCircle></AddCircle></IconButton>
        </Stack>
    </Sheet>)
}

export default CoursePage;
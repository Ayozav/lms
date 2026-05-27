import { useState, useMemo } from 'react';
import Profile from '../student/Profile.tsx';
import Timetable from '../timetable/Timetable.tsx';
import { mockTeachers } from '../teachers/Teacher.tsx';
import TeachersTable from '../teachers/TeachersTable.tsx';
import PesronalNote from '../notes/PesronalNote.tsx';
// import Welcome from './Welcome_component.tsx';
import StudentImage from '../assets/student_photo.jpg';

import {
    Box, CssBaseline,
    Drawer, List,
    ListItem, ListItemButton,
    ListItemIcon, ListItemText,
    Toolbar, Typography,
    Paper,
    IconButton,
    createTheme, ThemeProvider,
    Button,
} from '@mui/material';

import {
    School as KnowledgeIcon,
    Schedule as ScheduleIcon,
    People as OdnogruppnikiIcon,
    Brightness4 as DarkModeIcon,
    Brightness7 as LightModeIcon,
    ModeEdit as PenIcon,
    Logout,
    FolderShared as TeachersIcon,
} from '@mui/icons-material';

const drawerWidth = 240;

const menu_items = [
    { text: 'Профиль', icon: <img src={StudentImage} alt="profile" style={{ width: 100, height: 100, borderRadius: 115, marginRight: '12px' }} />, id:'profile'},
    {text: 'Моя группа', icon: <OdnogruppnikiIcon />, id: ''},
    { text: 'Расписание', icon: <ScheduleIcon />, id: 'schedule' },
    { text: 'Знание', icon: <KnowledgeIcon />, id: 'knowledge' },
    { text: 'Преподаватели', icon: <TeachersIcon />, id: 'teachers' },
    { text: 'Перо', icon: <PenIcon />, id: 'notes' },
    // {text: 'Выйти', icon: <Logout/>, id: 'logout', styled={}, 
];

//я честно хз как выход вписать в общество, потому что эта нетакуся должна быть снизу
const logout_button = {text: 'Выйти', icon: <Logout/>, id: 'logout'}


const LmsDarkTheme = createTheme({
    palette: {
        mode: 'dark', 
        background: {
            default:'#323232', 
            // paper:'#1F1B24' //google material?
        },
        text:{
            primary:'#ffffff',
            secondary:'#b0b0b0',
        }
    }
    
})

// ToDO: подобрать цвета на светлую тему
const LmsLigthTheme = createTheme({
    palette: {
        mode: 'light', 
        background: {
            default:'#E6E6E6', //чуть серее 
            // paper:'#' // пока нет необходимости, но может в будущем
        },
        text: {
            primary:'#000000',
        }
    }
    
})


function LogOutRedirect() {
    return (
        <Button>
    {/* <LogOut /> */}
        </Button>
        
    )
};

function PersonalNoteFrame() {
    return <PesronalNote />;
}

// function Group() {
//     return < Group/>;
// }

function TimetableFrame() {
    return (
        <Timetable
            groupName="ИС2-Б23"
            timetableData={[
                {
                    id: '001',
                    subject_name: 'Теория вероятностей',
                    teacher: 'Перегуда А. И.',
                    classroom: '2-512',
                    is_appointed: true,
                    day: 0, // понедельник
                    time: 0, // первая пара
                },
                {
                    id: '002',
                    subject_name: 'Физика',
                    teacher: 'Саввин В. П.',
                    classroom: '1-401',
                    is_appointed: true,
                    day: 0,
                    time: 1,
                },
                {
                    id: '003',
                    subject_name: 'Английский язык',
                    teacher: 'Воробьева Е. Н.',
                    classroom: '3-311',
                    is_appointed: true,
                    day: 1, // вторник
                    time: 0,
                },
                {
                    id: '004',
                    subject_name: 'Физкультура',
                    teacher: 'Полевой А. А.',
                    classroom: 'СК',
                    is_appointed: true,
                    day: 1,
                    time: 1,
                },
                {
                    id: '005',
                    subject_name: 'Базы данных',
                    teacher: 'Цветкова О. А.',
                    classroom: '2-611',
                    is_appointed: true,
                    day: 6,
                    time: 0,
                },
                {
                    id: '006',
                    subject_name: 'Технологии программирования',
                    teacher: 'Карандашов В.',
                    classroom: '2-610',
                    is_appointed: true,
                    day: 4,
                    time: 5,
                },
                {
                    id: '006',
                    subject_name: 'Технологии программироания',
                    teacher: 'Карандашов В.',
                    classroom: '2-610',
                    is_appointed: true,
                    day: 3,
                    time: 5,
                },
                {
                    id: '007',
                    subject_name: 'Дискретная математика',
                    teacher: 'Ермаков С. В.',
                    classroom: '2-512',
                    is_appointed: true,
                    day: 3,
                    time: 0,
                },
            ]}
            loading={false}
        />
    );
}

function ProfileFrame() {
    return (
        <Profile
            fullName="Ошеровская Дарья Викторовна"
            age={20}
            birthDate="03.07.2005"
            course={3}
            emailAddress="frontend.hater@yes.me"
            gradeLevel="Бакалавриат"
            group="ИС2-Б23"
            institute="ОИКС"
            phoneNumber="8 800 535 35 35"
            program_code="09.03.02"
            study_mode="Очная"
            loading={false}
        />
    );
}

function TeachersFrame() {
    return (
        <>
            <Typography variant="h5" sx={{ p: 3 }}>
                Список преподавателей
            </Typography>
            <TeachersTable teachers={mockTeachers} />
        </>
    );
}

function MainFrame(cards: any, TabChange: Function) {
    return (
        <Box
            sx={{
                minHeight: '100vh',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                p: 3,
            }}
        >
            <Box
                sx={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    justifyContent: 'center',
                    gap: 3,
                    maxWidth: '1100px',
                    width: '100%',
                }}
            >
                {cards.map((square: any) => (
                    <Box
                        key={square.id}
                        sx={{
                            width: { xs: '100%', sm: 'calc(50% - 12px)', md: '250px' },
                            flexShrink: 0,
                        }}
                    >
                        <Paper
                            elevation={3}
                            sx={{
                                p: 3,
                                height: 200,
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'center',
                                alignItems: 'center',
                                borderRadius: 4,
                                backgroundColor: square.color,
                                color: 'white',
                                transition: 'transform 0.2s, box-shadow 0.2s',
                                cursor: 'pointer',
                                textAlign: 'center', // выравнивание по центру
                                '&:hover': {
                                    transform: 'translateY(-4px)',
                                    boxShadow: 6,
                                },
                            }}
                            onClick={() => TabChange(square.tabId)}
                        >
                            <Typography
                                variant="h5"
                                component="div"
                                gutterBottom
                                sx={{ width: '100%', wordBreak: 'break-word' }}
                            >
                                {square.title}
                            </Typography>
                            {square.content && (
                                <Typography variant="body2" align="center" sx={{ width: '100%' }}>
                                    {square.content}
                                </Typography>
                            )}
                        </Paper>
                    </Box>
                ))}
            </Box>
        </Box>
    );
}

function MainPage() {
    const [selectedTab, setSelectedTab] = useState('knowledge');
    const [mode, setMode] = useState<'light' | 'dark'>('light');

    const toggleTheme = () => {
        setMode((prev) => (prev === 'light' ? 'dark' : 'light'));
    };

    const theme = mode === 'dark' ? LmsDarkTheme : LmsLigthTheme;

    const TabChange = (tabId: string) => {
        setSelectedTab(tabId);
    };

    const cards = [
        { id: 1, title: 'Расписание', color: '#ff9289', content: 'учебных занятий', tabId: 'schedule' },
        { id: 2, title: 'Преподаватели', color: '#64f074', tabId: 'teachers' },
        { id: 3, title: 'Моя группа', color: '#fab168', tabId: 'mygroup' },
        { id: 4, title: 'Знание', color: '#ffdd55', content: 'Внутреннее устройство ИАТЭ НИЯУ МИФИ', tabId: 'knowledge' }, //TODO: надо ли оно?
        { id: 5, title: 'Перо', color: '#6fa1f3', content: 'Учебные заметки', tabId: 'notes' },
];

    const drawer = (
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <Toolbar sx={{ justifyContent: 'space-between' }}>
                <Typography variant="h4" noWrap component="div" sx={{fontFamily:'Plaster'}}>
                    LMS
                </Typography>
                <IconButton onClick={toggleTheme} color="inherit">
                    {/* ToDo: icons...... */}
                    {mode === 'dark' ? <LightModeIcon /> : <DarkModeIcon />} 
                </IconButton>
            </Toolbar>
            <List sx={{ flexGrow: 1 }}>
                {menu_items.map((item) => (
                    <ListItem key={item.text} disablePadding>
                        <ListItemButton
                            selected={selectedTab === item.id}
                            onClick={() => TabChange(item.id)}
                        >
                            <ListItemIcon>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.text} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
            <List>
                <ListItem disablePadding>
                    <ListItemButton
                        selected={selectedTab === logout_button.id}
                        onClick={() => TabChange(logout_button.id)}
                    >
                        <ListItemIcon>{logout_button.icon}</ListItemIcon>
                        <ListItemText primary={logout_button.text} />
                    </ListItemButton>
                </ListItem>
            </List>
        </Box>
    );

    const frameRender = () => {
        switch (selectedTab) {
            case 'logout':
                return <LogOutRedirect/>;
            case 'notes':
                return <PersonalNoteFrame />;
            case 'schedule':
                return <TimetableFrame />;
            case 'profile':
                return <ProfileFrame />;
            case 'teachers':
                return <TeachersFrame />;
            default:
                return (
                    <>
                        {MainFrame(cards, TabChange)}
                        {/* <Welcome /> */}
                    </>
                );
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box sx={{ display: 'flex' }}>
                <Box
                    component="nav"
                    sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
                >
                    <Drawer
                        variant="permanent"
                        sx={{
                            display: { xs: 'none', sm: 'block' },
                            '& .MuiDrawer-paper': {
                                boxSizing: 'border-box',
                                width: drawerWidth,
                                bgcolor: 'background.default'
                            },
                        }}
                        open
                    >
                        {drawer}
                    </Drawer>
                </Box>
                <Box component="main" sx={{ flexGrow: 1 }}>
                    {frameRender()}
                </Box>
            </Box>
        </ThemeProvider>
    );
}

export default MainPage;

// проверка
//         const testBackend = async () => {
//             console.log('Кнопка нажата'); 
//             try {
//                 const response = await fetch('http://127.0.0.1:4040/v1/semesters?page=1');
//                 const data = await response.json();
//                 console.log('✅ Бэкенд ответил:', data);
//             } catch (error) {
//                 console.error('❌ Ошибка:', error);
//             }
//         };
//         <button onClick={testBackend}>Проверить бэкенд</button>
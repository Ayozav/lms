import { useState, useEffect } from 'react';
import Profile from '../student/Profile.tsx';
import Timetable from '../timetable/Timetable.tsx';
import { mockTeachers } from '../teachers/Teacher.tsx';
import TeachersTable from '../teachers/TeachersTable.tsx';
import {PersonalNote} from '../pero/PesronalNote.tsx';
// import Welcome from './Welcome_component.tsx';
import StudentImage from '../assets/student_photo.jpg';

//новые иконки (к сожалению все руинится)
// import GroupIcon from "../assets/icons/Group.svg";
// import TimetableIcon from "../assets/icons/Time.svg";
// import KnowlegeIcon from "../assets/icons/Atom_light.svg";
// import TeacherIcon from '../assets/icons/Tie.svg';
// import Lightmode from '../assets/icons/Sun.svg';
// import Darkmode from '../assets/icons/Moon.svg';
// import PensIcon from '../assets/icons/Edit.svg';
// import LogOut from '../assets/icons/Log_Out.svg';

//profile + user
import {userService} from '../api/servieces/usesrService.tsx';
import {type User} from '../api/modules/user.tsx';
// import { type Grade } from '../api/modules/grade.tsx';

//teachers + 
// import {} from 

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
    // People as OdnogruppnikiIcon,
    Brightness4 as DarkModeIcon,
    Brightness7 as LightModeIcon,
    ModeEdit as PenIcon,
    Logout,
    FolderShared as TeachersIcon,
} from '@mui/icons-material';
// import { gradeService } from '../api/servieces/gradeService.tsx';

const drawerWidth = 240;

// animation="wave" 
const menu_items = [
    { text: 'Профиль', icon: <img src={StudentImage} alt="profile" style={{ width: 100, height: 100, borderRadius: 115, marginRight: '12px', fontFamily:'Jost'}} />, id:'profile'},
    // {text: 'Моя группа', icon: <OdnogruppnikiIcon />, id: ''},
    { text: 'Расписание', icon: <ScheduleIcon />, sx:{fontFamily:"Jost"}, id: 'schedule',},
    { text: 'Знание', icon: <KnowledgeIcon />, id: 'knowledge' },
    { text: 'Преподаватели', icon: <TeachersIcon />, id: 'teachers' },
    { text: 'Перо', icon: <PenIcon />, id: 'notes' },
    // {text: 'Выйти', icon: <Logout/>, id: 'logout', styled={}, 
];

//я честно хз как выход вписать в общество, потому что эта нетакуся должна быть снизу
const logout_button = {text: 'Выйти', icon: <Logout/>, id: 'logout'}

// цветовые темы
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

const LmsLigthTheme = createTheme({
    palette: {
        mode: 'light', 
        background: {
            // default:'#E6E6E6', //серее 
            default:'#ebeaea', //чуть серее
            // paper:'#' // пока нет необходимости, но может в будущем
        },
        text: {
            primary:'#000000',
        },
    }
})

const LmsDrawnerThemeLight = createTheme({
    palette: {
        mode:'dark',
        background: {
            default:'#dedede',
        },
        text: {
            primary:'#000000',
        },
        action: {
            active: '#000000',       //обычные иконки
            hover: '#c1c1c1',        //наведение
            selected: '#000000',     //пункт выбран
            disabled: '#999999',
        },
    },
    typography: {
        fontFamily: '"Jost", sans-serif',
  },
})

const LmsDrawnerThemeDark = createTheme({
    palette: {
        mode:'light',
        background: {
            default: '#4b4b4b',
        },
        text: {
            primary:'#ffff'
        },
        action: {
            active: '#ffff',       // обычные иконки
            hover: '#555555',        // при наведении
            selected: '#ffffff',     // когда пункт выбран
            disabled: '#999999',
        },
    },
    typography: {
        fontFamily: '"Jost", sans-serif',
  },
})


function LogOutRedirect() {
    return (
        <Button>
    {/* <LogOut /> */}
        </Button>
        
    )
};

function PersonalNoteFrame() {
    return <PersonalNote />;
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
    const [profile, setProfile] = useState<User | null>(null);
    // const [loading] = useState(true);

  useEffect(() => {
    userService.getById(1)
      .then(data => {
        setProfile(data);
      })
      .catch(error => console.error('Ошибка загрузки профиля:', error))
    //   .finally(() => setLoading(false));
  }, []);

    if (!profile) {
        return <Typography sx={{ p: 5 }} color="error">Не удалось загрузить профиль</Typography>;
    }

    return (
        <Profile
            // fullName={`${profile.lastName} ${profile.firstName} ${profile.patronymic}`} ПОТОМУ ЧТО ДА СПАСИБО ДАША 
            lastName={profile.lastName}
            firstName={profile.firstName}
            patronymic={profile.patronymic} //ToDo: sPSODKFIJODDJODPSOSKSKDIWU8278374ZBYD-[mino;liqhwckajsnck!OIQNWI!MLdnsdmd]
            // age={calculateAge(profile?.birthDate)}
            age={0}
            birthDate={profile.birthDate}
            // course={profile.course} )))))))))))))0
            // emailAddress={'student@example.com'}
            // gradeLevel={'нет'}
            // group={}
            // institute={}
            // phoneNumber={}
            // program_code={}
            // study_mode={}
            // loading={false}
        />
  );
}

//статичный пропс (НА ВСЯКИЙ случай.)
// function ProfileFrame() {
//     return (
        // <Profile
        //     fullName="Ошеровская Дарья Викторовна"
        //     age={20}
        //     birthDate="03.07.2005"
        //     course={3}
        //     emailAddress="frontend.hater@yes.me"
        //     gradeLevel="Бакалавриат"
        //     group="ИС2-Б23"
        //     institute="ОИКС"
        //     phoneNumber="8 800 535 35 35"
        //     program_code="09.03.02"
        //     study_mode="Очная"
        //     loading={false}
        // />
//     );
// }

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
    const drawerTheme = mode === 'dark' ? LmsDrawnerThemeDark : LmsDrawnerThemeLight;

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
                    </>
                );
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box sx={{ display: 'flex' }}>
                <Box component="nav" sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}>
                    <ThemeProvider theme={drawerTheme}> {/* Вложенный провайдер для drawer */}
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
                    </ThemeProvider>
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
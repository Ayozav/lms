import { useState } from 'react';
import Profile from '../student/Profile.tsx';
import Timetable from '../timetable/Timetable.tsx'
import { mockTeachers } from '../teachers/Teacher.tsx'; 
import TeachersTable from '../teachers/TeachersTable.tsx'
import PesronalNote from '../notes/PesronalNote.tsx'

import {
    Box, CssBaseline,
    Drawer, List,
    ListItem, ListItemButton,
    ListItemIcon, ListItemText,
    Toolbar, Typography,
    Paper, Grid,
} from '@mui/material';


import {
    School as KnowledgeIcon,
    Schedule as ScheduleIcon,
    AccountCircle as AccountIcon,
    People as TeachersIcon
} from '@mui/icons-material';


const drawerWidth = 240;

const menu_items = [
    { text: 'Знание', icon: <KnowledgeIcon />, id: 'knowledge' },
    { text: 'Расписание', icon: <ScheduleIcon />, id: 'schedule' },
    { text: 'Личный кабинет', icon: <AccountIcon />, id: 'profile' },
    { text: 'Преподаватели', icon: <TeachersIcon />, id: 'teachers' },
    { text: 'Перо', icon:< TeachersIcon/>, id:'node'},
];



function PersonalNoteFrame() {
    return <PesronalNote/>;
}

function TimetableFrame() {
    return <Timetable 
        groupName='GROUP' 
        timetableData={
            [
                {   
                    id: '001', 
                    subject_name: 'Теория вероятностей',
                    teacher: 'Перегуда А. И.',
                    classroom: '2-512',
                    is_appointed: true,
                    day: 0, 
                    time: 0
                }
            ]
        }
        loading={false}
    />;
}

function ProfileFrame() {
    return <Profile 
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
    />;
}

function TeachersFrame() {
    return <>
        <Typography variant="h5" sx={{ p: 3 }}>Список преподавателей</Typography>
        <TeachersTable teachers={mockTeachers} />
    </>;
}

function MainFrame(cards: any, TabChange: Function) {
    return <Box sx={{ flexGrow: 1, p: 3, backgroundColor: '#f5f5f5', minHeight: '100vh',}}>
        {/* Карточки-кнопочки-переключалки между Frame'ами */}
        <Grid container spacing={3}>
            {
                cards.map(
                    (square: any) => 
                        <Grid key={square.id} size={{xs: 12, sm: 8, md: 3}}>
                            <Paper elevation={3}
                                sx={{
                                    p: 3, height: 200,
                                    display: 'flex', flexDirection: 'column',
                                    justifyContent: 'center', alignItems: 'center',
                                    borderRadius: 4,
                                    backgroundColor: square.color,
                                    color: 'white',
                                    transition: 'transform 0.2s, box-shadow 0.2s',
                                    cursor: 'pointer',
                                    '&:hover': {
                                        transform: 'translateY(-4px)',
                                        boxShadow: 6,
                                    },
                                }}
                                onClick={() => TabChange(square.tabId)}>
                                <Typography variant="h5" component="div" gutterBottom>
                                    {square.title}
                                </Typography>
                                <Typography variant="body2" align="center">
                                    {square.content}
                                </Typography>
                            </Paper>
                        </Grid>
                )}
        </Grid>
        {/* ??? Что это за японский Бог ??? */}
        {/* 
            <Box sx={{ mt: 4, p: 2, backgroundColor: 'white', borderRadius: 2 }}>
            <Typography variant="body1" color="text.secondary">
                Здесь дополнительная информация для раздела "{menu_items.find(item => item.id === selectedTab)?.text}"
            </Typography>
            </Box> 
        */}
    </Box>;
}

function MainPage() {

    // Задаём нашу страницу в виде жёстких табов, начиная со страницы "моя группа" (почему knowledge???)
    const [selectedTab, setSelectedTab] = useState('knowledge');

    // Делаем стрелочную функцию, потому что хотим побольше контроля над переключением таба.
    const TabChange = (tabId: string) => {
        setSelectedTab(tabId);
    }

    // ToDo: превратить ЭТО в нормальные компоненты.
    const cards = [
        { id: 1, title: 'Расписание', color: '#ff9289', content: 'учебных занятий', tabId: 'schedule' },
        { id: 2, title: 'Профиль', color: '#87f694', content: 'Личный кабинет', tabId: 'profile' },
        { id: 3, title: 'Моя группа', color: '#fac086', content: 'хз', tabId: 'knowledge' },
        { id: 4, title: 'Знание', color: '#ffe57e', content: 'Внутреннее устройство ИАТЭ НИЯУ МИФИ', tabId: 'knowledge' },
        { id: 5, title:'Перо', color: '#6fa1f3', content: 'Учебные заметки', tableId: 'notes' }
    ];

    // Потом жёстко пилим менюшку-ящик (drawer) под бок.
    // И делаем её так, чтобы оно работало ещё и на телефоне.
    const drawer = (
        <div>
        <Toolbar>
            <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
            Меню
            </Typography>
        </Toolbar>
        <List>
            {
                menu_items.map(
                    (item) => (
                    <ListItem key={item.text} disablePadding>
                        <ListItemButton
                        selected={selectedTab === item.id}
                        onClick={() => TabChange(item.id)}>
                            <ListItemIcon>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.text} />
                            </ListItemButton>
            </ListItem>
            ))}
        </List>
        </div>
    );

    // Рабочая переключалка, работающая по принципу "что нажали, то и получили"
    // Запускается ниже.
    const frameRender = () => {
        switch (selectedTab) {
        case 'notes':
            // Заметки
            return <PersonalNoteFrame />;
        case 'schedule':
            // Расписание на две недели
            return <TimetableFrame />
        case 'profile':
            // Информация о пользователе
            return <ProfileFrame />;
        case 'teachers':
            // Перечень преподавателей.
            return <TeachersFrame />;
        default: 
            // По умолчанию - главная страница
            return MainFrame(cards, TabChange);
        }
    };

    return <Box sx={{ display: 'flex' }}>
        {/* Кто он? Откуда он? Бог его знает... */}
        <CssBaseline />
        
        {/* Боковая панель */}
        <Box component="nav" sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}>
            <Drawer
                variant="permanent"
                // За что это отвечает ???
                sx={{
                        display: { xs: 'none', sm: 'block' }, 
                        '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                }}
            open>
                {drawer}
            </Drawer>
        </Box>
        {/* Основное окно всех Frame'ов */}
        <Box component="main" sx={{ flexGrow: 1 }}>
            {frameRender()}
        </Box>
    </Box>;
}

export default MainPage;

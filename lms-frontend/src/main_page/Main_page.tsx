// import React, { useState } from 'react'; НУ И БОЛЬНО ХОТЕЛОСЬ
import { useState } from 'react';
import Profile from '../student/profile.tsx';
import Timetable from '../timetable/Timetable.tsx'
// import TeacherProps from '../teachers/teachers_component.tsx';
import { mockTeachers } from '../teachers/teachers_component.tsx'; 
import {TeachersTable} from '../teachers/teachers.tsx'
import Pesronal_Note from '../notes/Pesronal_Note.tsx'

import {
  AppBar,
  Box,
  CssBaseline,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  Paper,
  Grid,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import {
  School as KnowledgeIcon,
  Schedule as ScheduleIcon,
  AccountCircle as AccountIcon,
  People as TeachersIcon
} from '@mui/icons-material';

import CreateIcon from '@mui/icons-material/Create';

const drawerWidth = 240;

const menu_items = [
  { text: 'Знание', icon: <KnowledgeIcon />, id: 'knowledge' },
  { text: 'Расписание', icon: <ScheduleIcon />, id: 'schedule' },
  { text: 'Личный кабинет', icon: <AccountIcon />, id: 'profile' },
  { text: 'Преподаватели', icon: <TeachersIcon />, id: 'teachers' },
  { text: 'Перо', icon:< TeachersIcon/>, id:'node'},
];

function Main_page() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const [selectedTab, setSelectedTab] = useState('knowledge');

  const TabChange = (tabId: string) => {
    setSelectedTab(tabId);
  }

  const squares = [
    { id: 1, title: 'Расписание', color: '#ff9289', content: 'учбеных занятий', tabId: 'schedule' },
    { id: 2, title: 'Профиль', color: '#87f694', content: 'Личный кабинет', tabId: 'profile' },
    { id: 3, title: 'Моя группа', color: '#fac086', content: 'хз', tabId: 'knowledge' },
    { id: 4, title: 'Знание', color: '#ffe57e', content: 'Внутреннее устройство ИАТЭ НИЯУ МИФИ', tabId: 'knowledge' },
    {id: 5, title:'Перо', color: '#6fa1f3', content: 'Учебные заметки', tableId: CreateIcon }
  ];

  const drawer = (
    <div>
      <Toolbar>
        <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
          Меню
        </Typography>
      </Toolbar>
      <List>
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
    </div>
  );

  // !!СВИТЧ ВЫБОРА!!
  const render = () => {
    switch (selectedTab) {
      case 'nodes':
        return (
          <Pesronal_Note>
            {/* ehhhh */}
          </Pesronal_Note>
        );
      case 'schedule':
        return (
          <Timetable 
            groupName='GROUP' 
            timetableData={[
              {
                id: '001', 
                subject_name: 'Теория вероятностей',
                teacher: 'Перегуда А. И.',
                classroom: '2-512',
                is_appointed: true,
                day: 0, 
                time: 0
              }
            ]} 
            loading={false}
          />
        );
      case 'profile':
        return (
          <Profile 
            profileData={{
              full_name: 'Ошеровская Дарья Викторовна',
              age: 20,
              b_date: 'dd.mm.yyyy',
              phone_number: 79161234567,
              email_adress: 'frontend.hater@yes.me',
              home_adress: 'adress',
              grade_level: 'Бакалавриат',
              country: 'ru',
              study_mode: 'Очная',
              institute: 'ОИКС',
              course: 3,
              program_code: '09.03.01',
              group: 'Ис2-б23',
              zachetka: 'СТО-12345',
              path_to_photo: '',
            }}
            loading={false}
            />
        );
      case 'teachers':
          return (
            <>
              <Typography variant="h5" sx={{ p: 3 }}>
                Список преподавателей
              </Typography>
                <TeachersTable teachers={mockTeachers} />
            </>
        );
      default: //main
        return (
          <Box sx={{
            flexGrow: 1,
            p: 3,
            backgroundColor: '#f5f5f5',
            minHeight: '100vh',
          }}>
            {/* квадраты с Grid */}
            <Grid container spacing={3}>
              {squares.map((square) => (
                <Grid key={square.id} size={{xs: 12, sm: 8, md: 3}}>
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
                      '&:hover': {
                        transform: 'translateY(-4px)',
                        boxShadow: 6,
                      },
                    }}
                    onClick={() => TabChange(square.tabId)}
                  >
                    <Typography variant="h5" component="div" gutterBottom>
                      {square.title}
                    </Typography>
                    <Typography variant="body2" align="center">
                      {square.content}
                    </Typography>
                  </Paper>
                </Grid>
              ))}
            </Grid>

            {/* <Box sx={{ mt: 4, p: 2, backgroundColor: 'white', borderRadius: 2 }}>
              <Typography variant="body1" color="text.secondary">
                Здесь дополнительная информация для раздела "{menu_items.find(item => item.id === selectedTab)?.text}"
              </Typography>
            </Box> */}
          </Box>
        );
    }
  }

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      
      
      {/* Боковая панель */}
      <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
      >
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
          open
        >
          {drawer}
        </Drawer>
      </Box>

      <Box component="main" sx={{ flexGrow: 1 }}>
        {render()}
      </Box>
    </Box>
  );
}

export default Main_page;
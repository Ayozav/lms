// import React, { useState } from 'react'; НУ И БОЛЬНО ХОТЕЛОСЬ
import { useState } from 'react';
import Profile from '../student/profile.tsx';
import Timetable from '../timetable/Timetable.tsx'
// import TeacherProps from '../teachers/teachers_component.tsx';
import { TeacherProfile } from '../teachers/teachers.tsx'; 

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
  Menu as MenuIcon,
  School as KnowledgeIcon,
  Schedule as ScheduleIcon,
  AccountCircle as AccountIcon,
  People as TeachersIcon,
} from '@mui/icons-material';

const drawerWidth = 240;

const menu_items = [
  { text: 'Знание', icon: <KnowledgeIcon />, id: 'knowledge' },
  { text: 'Расписание', icon: <ScheduleIcon />, id: 'schedule' },
  { text: 'Личный кабинет', icon: <AccountIcon />, id: 'profile' },
  { text: 'Преподаватели', icon: <TeachersIcon />, id: 'teachers' },
];

function Main_page() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const [mobileOpen, setMobileOpen] = useState(false);
  const [selectedTab, setSelectedTab] = useState('knowledge');

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const TabChange = (tabId: string) => {
    setSelectedTab(tabId);
    if (isMobile) setMobileOpen(false);
  }

  const squares = [
    { id: 1, title: 'Расписание', color: '#FF6B6B', content: 'Расписание группы', tabId: 'schedule' },
    { id: 2, title: 'Вход в систему', color: '#4ECDC4', content: 'Войти с использованием локальной учетной записи', tabId: 'profile' },
    { id: 3, title: 'Моя группа', color: '#45B7D1', content: 'Я и мой детский садик', tabId: 'knowledge' },
    { id: 4, title: '(Что-то ещё)', color: '#96CEB4', content: 'здесь могла быть ваша реклама', tabId: 'knowledge' },
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
          // <Typography variant="h5" sx={{ p: 3 }}>
          //   Список преподавателей
          // </Typography>
          <TeacherProfile 
            teacher_info={{
              full_name: 'Перегуда Аркадий Иванович',
              institute: 'ОИКС',
              regalis: 'самый крутой булочка',
              b_date: '03.07.1800'
            }}
            />
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

            <Box sx={{ mt: 4, p: 2, backgroundColor: 'white', borderRadius: 2 }}>
              <Typography variant="body1" color="text.secondary">
                Здесь дополнительная информация для раздела "{menu_items.find(item => item.id === selectedTab)?.text}"
              </Typography>
            </Box>
          </Box>
        );
    }
  }

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      
      {/* Верхняя панель для мобильных устройств */}
      <AppBar
        position="fixed"
        sx={{
          width: { sm: `calc(100% - ${drawerWidth}px)` },
          ml: { sm: `${drawerWidth}px` },
          display: { sm: 'none' },
        }}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component="div">
            {menu_items.find(item => item.id === selectedTab)?.text || 'Главная'}
          </Typography>
        </Toolbar>
      </AppBar>

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
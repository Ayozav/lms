// import type { teacher_profile } from "./teachers_component";
import type { TeacherProps } from "./teachers_component";

import React from 'react';
import {Box,
    Typography,
    Avatar,
    Chip
} from '@mui/material';
import { SignalWifiStatusbarConnectedNoInternet4TwoTone } from "@mui/icons-material";
// я хз че это

const instituteBox = {
    width: {md:'100%'},
    height: {md:'100%'},
    display:'flex'
};

// Все брейкпоинты (точки перелома) MUI:
// Ключ	Размер экрана	Тип устройства
// xs	0px - 600px	📱 Телефоны (маленькие)
// sm	600px - 900px	📱 Телефоны (большие)
// md	900px - 1200px	💻 Компьютеры (ноутбуки)
// lg	1200px - 1536px	🖥 Компьютеры (большие мониторы)
// xl	1536px+	📺 Очень большие экраны / TV

const TeacherProfile: React.FC<TeacherProps> = ({teacher_info}) => {
    switch (teacher_info.institute) {
        case 'ОИКС':
            return (
                <Box sx={instituteBox}>
                <Typography>{teacher_info.institute}</Typography>
                </Box>
            );
        case 'ОЯФИТ':
            return (
                <Box sx={instituteBox}>
                <Typography>{teacher_info.institute}</Typography>
                </Box>
            );
        case 'ЛаПлаз':
            return (
                <Box sx={instituteBox}>
                <Typography>{teacher_info.institute}</Typography>
                </Box>
            );
        case 'ОБТ':
            return (
                <Box sx={instituteBox}>
                <Typography>{teacher_info.institute}</Typography>
                </Box>
            );
        case 'ОСЭН':
            return (
                <Box sx={instituteBox}>
                <Typography>{teacher_info.institute}</Typography>
                </Box>
            );
        default:
            return (
                <Box sx={instituteBox}>
                <Typography>Внештатный преподаватель</Typography>
                </Box>
            );
    }
}
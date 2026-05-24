import React from 'react';
import { Box, Icon, Typography } from '@mui/material';

export interface teacher_profile {
    full_name: string;   // лучше string, а не String (примитив)
    institute: string;
    regalis: string;
    email: string;
    kabinet: string;
}

import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import ConnectedTvIcon from '@mui/icons-material/ConnectedTv';
import WorkIcon from '@mui/icons-material/Work';
import StreamIcon from '@mui/icons-material/Stream';
import ScatterPlotIcon from '@mui/icons-material/ScatterPlot';

export interface TeacherProps {
    teacher_info: teacher_profile;
}


export const TeacherProfile: React.FC<TeacherProps> = ({ teacher_info }) => {
    switch (teacher_info.institute) {
        case 'ОИКС':
            return (
                <Box sx={{display:'flex', alignItems:'center', gap:1, md:'100px'}}>
                    <ConnectedTvIcon sx={{color:'#8EA6ED'}} />
                    <Typography sx={{ color: '#8EA6ED' }}>{teacher_info.institute}</Typography>
                </Box>
            )
        case 'ОЯФИТ':
            return (
                <Box sx={{display:'flex', alignItems:'center', gap:0}}>
                    <ScatterPlotIcon sx={{color:'#BF8EED'}} />
                    <Typography sx={{ color: '#BF8EED' }}>{teacher_info.institute}</Typography>;
                </Box>
            )
          case 'ЛаПлаз':
            return (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <StreamIcon sx={{color: '#EDB18E' }} />
                    <Typography sx={{ color: '#EDB18E' }}>{teacher_info.institute}</Typography>
                </Box>
            )
        case 'ОБТ':
            return (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <WorkIcon sx={{ color: '#ED8E93' }} />
                    return <Typography sx={{color: '#ED8E93'}}>{teacher_info.institute}</Typography>;
                </Box>
            )

        case 'ОСЭН':
            return (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <WorkIcon sx={{ color: '#ED8ECB' }} />
                    <Typography sx={{ color:'#ED8ECB'}}>{teacher_info.institute}</Typography>;
                </Box>
            )
        default:
            return <Typography>Внештатный преподаватель</Typography>;
    }
};

// Мок заглушка
export const mockTeachers: teacher_profile[] = [
    {
        full_name: 'Перегуда Аркадий Иванович',
        institute: 'ОИКС',
        regalis: 'д.ф.-м.н., профессор',
        email: 'pereguda@iate.obninsk.ru',
        kabinet: '2-506',

    },
    {
        full_name: 'Нахабов Александр Владимирович',
        institute: 'ОЯФИТ',
        regalis: 'кандидат технических наук, доцент',
        email: 'AVNakhabov@mephi.ru',
        kabinet: '3-419',

    },
    {
        full_name: 'name',
        institute: 'institute',
        regalis: 'regalis',
        email: 'email',
        kabinet: 'kabinet',

    },
];
import type { teacher_profile } from "./teachers_component";
// import type { TeacherProps } from "./teachers_component";
import {TeacherProfile} from "./teachers_component";

import React from 'react';
import {Box,
        Paper,
        Table,
        TableBody,
        TableCell,
        TableContainer,
        TableHead,
        TableRow,
        Typography,
} from '@mui/material';
// import { Height } from "@mui/icons-material";
// import { SignalWifiStatusbarConnectedNoInternet4TwoTone } from "@mui/icons-material";
// я хз че это

// вонючий бокс гори в аду чмоня
const instituteBox = {
    width: {md:'100%'},
    height: {md:'100%'},
    display:'flex'
};

const fio_box = {
    width:{md:'100%'}, 
    Height:{md:'100%'},
    display:'flex'
};

// Все брейкпоинты MUI:
// Ключ	Размер экрана	Тип устройства
// xs	0px - 600px	    📱 Телефоны (маленькие)
// sm	600px - 900px	📱 Телефоны (большие)
// md	900px - 1200px	💻 Компьютеры (ноутбуки)
// lg	1200px - 1536px	🖥 Компьютеры (большие мониторы)
// xl	1536px+	        📺 Очень большие экраны / TV

interface TeachersTableProps {
  teachers: teacher_profile[]; // массив преподавателей
}

export const TeachersTable: React.FC<TeachersTableProps> = ({ teachers }) => {
  const formatDate = (dateString: string) => {
    if (!dateString) return '—';
    try {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) return dateString;
      return date.toLocaleDateString('ru-RU');
    } catch {
      return dateString;
    }
  };

  return (
    <TableContainer component={Paper} sx={{ maxWidth: '100%', overflowX: 'auto' }}>
      <Table sx={{ minWidth: 650 }} aria-label="таблица преподавателей">
        <TableHead>
          <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
            <TableCell>ФИО</TableCell>
            <TableCell>ОТделение</TableCell>
            <TableCell>Должность</TableCell>
            <TableCell>Электронная почта</TableCell>
            <TableCell>Кабинет</TableCell>
            {/* <TableCell>Электронная почта</TableCell> */}
          </TableRow>
        </TableHead>
        <TableBody>
          {teachers.map((teacher, index) => (
            <TableRow
              key={index}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                <Typography variant="body2" sx={{ fontWeight: 500 }}>
                  {teacher.full_name}
                </Typography>
              </TableCell>
              
              <TableCell sx={{ width: '200px' }}> 
                <TeacherProfile teacher_info={teacher} />
              </TableCell>
              
              <TableCell>{teacher.regalis || '—'}</TableCell>

              <TableCell>{formatDate(teacher.email)}</TableCell>

              <TableCell>{formatDate(teacher.kabinet)}</TableCell>

            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};
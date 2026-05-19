import React from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Box,
  Skeleton,
} from '@mui/material';

import type {
  Subject_props,
  TimetableProps
} from '../timetable/Timetable_component';

import {DAYS, 
  // TIMESLOTS_kindergarten,
  // TIMESLOTS_high, 
  DEFAULT_TIMESLOTS
} from './Timetable_component'

const Timetable: React.FC<TimetableProps> = ({
  groupName,
  timetableData,
  loading,
}) => {
  // получение предмета для ячейки
  const getLessonForCell = (day: number, timeSlot: number): Subject_props | undefined => {
    return timetableData.find(
      (lesson) => lesson.day === day && lesson.time === timeSlot
    );
  };

  // загрузка
  if (loading) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography variant="h5" gutterBottom>
          {groupName}
        </Typography>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: 'bold' }}>Время / День</TableCell>
                {DAYS.map((day) => (
                  <TableCell key={day} sx={{ fontWeight: 'bold' }}>
                    {day}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {DEFAULT_TIMESLOTS.map((time, idx) => (
                <TableRow key={idx}>
                  <TableCell component="th" scope="row">
                    {time}
                  </TableCell>
                  {DAYS.map((_, dayIdx) => (
                    <TableCell key={dayIdx}>
                      <Skeleton variant="rectangular" height={60} animation="wave" />
                    </TableCell>
                  ))}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    );
  }

  // основной рендер таблицы
  return (
    <Box sx={{ p: 2, width: '100%', overflowX: 'auto' }}>
      <Typography variant="h5" gutterBottom sx={{ mb: 2 }}>
        Расписание – {groupName}
      </Typography>
      <TableContainer component={Paper} sx={{ boxShadow: 3 }}>
        <Table sx={{ minWidth: 600 }}>
          <TableHead>
            <TableRow>
              <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f0f0f0' }}>
                Время / День
              </TableCell>
              {DAYS.map((day) => (
                <TableCell key={day} align="center" sx={{ fontWeight: 'bold' }}>
                  {day}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {DEFAULT_TIMESLOTS.map((time, timeIdx) => (
              <TableRow key={timeIdx}>
                <TableCell component="th" scope="row" sx={{ fontWeight: 'medium' }}>
                  {time}
                </TableCell>
                {DAYS.map((_, dayIdx) => {
                  const lesson = getLessonForCell(dayIdx, timeIdx);
                  return (
                    <TableCell key={dayIdx} align="center" sx={{ padding: 1 }}>
                      {lesson ? (
                        <Box>
                          <Typography variant="body1" sx={{ fontWeight:"bold"}}>
                            {lesson.subject_name}
                          </Typography>
                          <Typography variant="body2" color="textSecondary">
                            {lesson.teacher}
                          </Typography>
                          <Typography variant="caption" color="textSecondary">
                            ауд. {lesson.classroom}
                          </Typography>
                        </Box>
                      ) : (
                        <Typography variant="body2" color="textSecondary">
                          —
                        </Typography>
                      )}
                    </TableCell>
                  );
                })}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Timetable;
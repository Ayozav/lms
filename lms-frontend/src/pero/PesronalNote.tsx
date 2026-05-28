import React, { useState, useEffect } from 'react';

import {
  Box,
  Typography,
  CircularProgress,
  Alert,
  Card,
  CardContent,
  Divider,
  Chip,
  Tabs,
  Tab,
  Paper,
} from '@mui/material';
import { 
    Assignment as AssignmentIcon, 
    Note as NoteIcon 
} from '@mui/icons-material';

import { attachedHomeworkService } from '../api/servieces/attachedHomeworkService';
import { homeworkService } from '../api/servieces/homeworkService';
import { getNotes } from '../api/servieces/noteService';
import { userService } from '../api/servieces/usesrService';
import type { AttachedHomework } from './HomeworkInterfaces';
import type { Homework } from '../api/modules/homework';
import type { Note } from './HomeworkInterfaces';

interface AttachedHomeworkWithHomework extends AttachedHomework {
  homeworkDetails?: Homework;
}

export const PersonalNote: React.FC = () => {
  const [tabValue, setTabValue] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [attachedHomeworks, setAttachedHomeworks] = useState<AttachedHomeworkWithHomework[]>([]);
  const [notes, setNotes] = useState<Note[]>([]);

  const getStudentId = async(): Promise<number> => {
    try {
      const profile = await userService.getById(1);
      return profile.id;
    } catch {
      throw new Error('Не удалось загрузить данные');
    }
};

useEffect(() => {
    const fetchData = async() => {
        setLoading(true);
        setError(null);
        try {
            const studentId = await getStudentId();
            const attachedList = await attachedHomeworkService.getByStudent(studentId);
            const enriched = await Promise.all(
            attachedList.map(async (ah) => {
                try {
                const homework = await homeworkService.getById(ah.homeworkId);
                return { ...ah, homeworkDetails: homework };
                } catch {
                return { ...ah, homeworkDetails: undefined };
                }
            })
            );
            setAttachedHomeworks(enriched);
            const userNotes = await getNotes();
            setNotes(userNotes);
        } catch (err: any) {
            setError(err.message || 'Ошибка загрузки данных');
        } finally {
            setLoading(false);
        }
    };
    fetchData();
    }, 
[]);

const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
        setTabValue(newValue);
};

const formatDate = (dateStr?: string) => {
    if (!dateStr) return '—';
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) return '—';
        return date.toLocaleDateString('ru-RU', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
  };

    if (loading) {
        return (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '50vh' }}>
            <CircularProgress />
        </Box>
        );
    }

    if (error) {
        return (
        <Box sx={{m: 3}}>
            <Alert severity="error">{error}</Alert>
        </Box>
        );
    }

    return (
        <Box sx={{p: {xs: 2, md:4}}}>
            <Paper elevation={0} sx={{mb: 3, p: 2, bgcolor: 'background.default'}}>
                {/* gap 1 или 2 */}
                <Typography variant="h4" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 2}}> 
                </Typography>
                {/* <Typography variant="subtitle1" color="text.secondary">
                    Учебные заметки и домашние задания
                </Typography> */}
            </Paper>

            <Tabs value={tabValue} onChange={handleTabChange} centered sx={{ mb: 3 }}>
                <Tab label="Домашние задания" icon={<AssignmentIcon />} iconPosition="start" />
                <Tab label="Заметки" icon={<NoteIcon />} iconPosition="start" />
            </Tabs>

            {/* Домашние задания */}
            {tabValue === 0 && (
                <Box>
                {attachedHomeworks.length === 0 ? (
                    <Alert severity="info">Нет назначенных заданий</Alert>
                ) : (
                    <Box
                    sx={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        gap: 3,
                    }}
                    >
                    {attachedHomeworks.map((ah) => (
                        <Box key={ah.id} sx={{ flex: '1 1 300px', minWidth: '280px' }}>
                        <Card variant="outlined" sx={{ height: '100%' }}>
                            <CardContent>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                                <Typography variant="h6" component="div">
                                {ah.homeworkDetails?.description || `Домашнее задание #${ah.homeworkId}`}
                                </Typography>
                                {ah.mark !== undefined && ah.mark !== null && (
                                <Chip
                                    label={`Оценка: ${ah.mark}`}
                                    color={ah.mark >= 3 ? 'success' : 'error'}
                                    size="small"
                                />
                                )}
                            </Box>
                            <Typography variant="body2" color="text.secondary" gutterBottom>
                                Дедлайн: {formatDate(ah.homeworkDetails?.deadline)}
                            </Typography>
                            {ah.homeworkDetails?.fileLink && (
                                <Typography variant="body2">
                                <a href={ah.homeworkDetails.fileLink} target="_blank" rel="noopener noreferrer">
                                    Ссылка на материал
                                </a>
                                </Typography>
                                )}
                                <Divider sx={{ my: 1.5 }} />
                                <Typography variant="caption" color="text.secondary">
                                    Статус: {(ah as any)?.status ?? 'Не сдано'}
                                    {/* {ah?.status ?? 'Не сдано'} */}
                                </Typography>
                            </CardContent>
                        </Card>
                        </Box>
                    ))}
                    </Box>
                )}
                </Box>
            )}

            {/* ЗАМЕКТИ */}
            {tabValue === 1 && (
                <Box>
                {notes.length === 0 ? (
                    <Alert severity="info">Заметок пока нет</Alert>
                ) : (
                    <Box
                    sx={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        gap: 3,
                    }}
                    >
                    {notes.map((note) => (
                        <Box key={note.id} sx={{ flex: '1 1 300px', minWidth: '280px' }}>
                        <Card variant="outlined">
                            <CardContent>
                            <Typography variant="h6" gutterBottom>
                                {(note as any).title || 'Заметка'}
                            </Typography>
                            <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                                {(note as any).content || (note as any).text || JSON.stringify(note)}
                            </Typography>
                            <Box sx={{ mt: 2, display: 'flex', justifyContent: 'flex-end' }}>
                                <Typography variant="caption" color="text.secondary">
                                {formatDate((note as any).updatedAt || (note as any).createdAt)}
                                </Typography>
                            </Box>
                            </CardContent>
                        </Card>
                        </Box>
                    ))}
                    </Box>
                )}
                </Box>
            )}
        </Box>
    );
};
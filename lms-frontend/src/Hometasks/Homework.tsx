import React, { useEffect, useState } from 'react';
import {
  Container, 
  Card, 
  CardContent, 
  Typography, 
  Button, 
  Dialog,
  DialogTitle, 
  DialogContent, 
  TextField,
  Box
} from '@mui/material';

// import { getAllHomeworks } from '../api/servieces/homeworkService';
// import {submitHomework} from '../api/servieces/homeworkService';
import {type Homework} from '../pero/HomeworkInterfaces';

// import { useAuth } from '../../store/authContext';

export const HomeworksPage: React.FC = () => {
  const [homeworks] = useState<Homework[]>([]);
  const [selectedHomework, setSelectedHomework] = useState<Homework | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [comment, setComment] = useState('');
//   const [loading, setLoading] = useState(false);
//   const { state } = useAuth();

useEffect(() => {
    if (file) console.log('Выбран файл:', file.name);
  }, [file]);



  return (
    <Container>
      <Typography variant="h4" gutterBottom>Домашние задания</Typography>
      <Box>
        {homeworks.map(hw => (
          <Box key={hw.id}>
            <Card>
              <CardContent>
                <Typography variant="h6">{`Задание #${hw.id}`}</Typography>
                <Typography color="textSecondary">
                  Дедлайн: {new Date(hw.deadline).toLocaleString()}
                </Typography>
                <Typography>{hw.description}</Typography>
                <Button variant="contained" onClick={() => setSelectedHomework(hw)}>
                  Сдать работу
                </Button>
              </CardContent>
            </Card>
          </Box>
        ))}
      </Box>

      <Dialog open={!!selectedHomework} onClose={() => setSelectedHomework(null)}>
        <DialogTitle>Сдача работы</DialogTitle>
        <DialogContent>
          <input type="file" onChange={(e) => setFile(e.target.files?.[0] || null)} />
          <TextField
            label="Комментарий" multiline rows={3} fullWidth margin="normal"
            value={comment} onChange={(e) => setComment(e.target.value)}
          />
        </DialogContent>
        {/* отправка (будет позже) */}
        {/* <DialogActions>
          <Button onClick={() => setSelectedHomework(null)}>Отмена</Button>
          <Button onClick={handleSubmit} disabled={loading}>
            {loading ? <CircularProgress size={24} /> : 'Отправить'}
          </Button>
        </DialogActions> */}
      </Dialog>
    </Container>
  );
};
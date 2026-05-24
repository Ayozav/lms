import {
    Paper, Table, TableBody,
    TableCell, TableContainer,
    TableHead, TableRow, Typography,
    Box
} from '@mui/material';

import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import ConnectedTvIcon from '@mui/icons-material/ConnectedTv';
import WorkIcon from '@mui/icons-material/Work';
import StreamIcon from '@mui/icons-material/Stream';
import ScatterPlotIcon from '@mui/icons-material/ScatterPlot';


import type { TeacherProps } from "./Teacher";


// Все брейкпоинты MUI:
// Ключ	Размер экрана	Тип устройства
// xs	0px - 600px	    📱 Телефоны (маленькие)
// sm	600px - 900px	📱 Телефоны (большие)
// md	900px - 1200px	💻 Компьютеры (ноутбуки)
// lg	1200px - 1536px	🖥 Компьютеры (большие мониторы)
// xl	1536px+	        📺 Очень большие экраны / TV


interface TeachersTableProps {
  teachers: TeacherProps[]; // массив преподавателей
}


function formatDate(dateString: string) {
    if (!dateString) return '—';
    try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return dateString;
        return date.toLocaleDateString('ru-RU');
    } catch {
        return dateString;
    }
};


const institute_to_color = {
    "ОИКС": "#8EA6ED",
    "ОЯФИТ": "#BF8EED",
    "ЛаПлаз": "#EDB18E",
    "ОБТ": "#ED8E93",
    "ОСЭН": "#ED8ECB"
} as const;

const institute_to_icon = {
    "ОИКС": <ConnectedTvIcon sx={{color: "#8EA6ED"}}/>,
    "ОЯФиТ": <ScatterPlotIcon sx={{color: "#BF8EED"}}/>,
    "ЛаПлаз": <StreamIcon sx={{color: "#EDB18E"}}/>,
    "ОБТ": <WorkIcon sx={{color: "#ED8E93"}}/>,
    "ОСЭН": <AccountBalanceIcon sx={{color: "#ED8ECB"}}/>
} as const;

function instituteBox(institute: string) {
    const color = institute_to_color[institute as keyof typeof institute_to_color] ?? "#CCCCCC";
    var icon = institute_to_icon[institute as keyof typeof institute_to_icon] ?? <WorkIcon />;

    return <Box sx={{display:'flex', alignItems:'center', gap:1, md:'100px'}}>
        {icon}
        <Typography sx={{ color: color }}>{institute}</Typography>
    </Box>;
}


function TeachersTable(props: TeachersTableProps) {
    var index = 0;
    return <TableContainer component={Paper} sx={{ maxWidth: '100%', overflowX: 'auto' }}>
        <Table sx={{ minWidth: 650 }} aria-label="таблица преподавателей">
            <TableHead>
                <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                    <TableCell>ФИО</TableCell>
                    <TableCell>Отделение</TableCell>
                    <TableCell>Должность</TableCell>
                    <TableCell>Электронная почта</TableCell>
                </TableRow>
            </TableHead>
        <TableBody>
            {props.teachers.map(
                (teacher: TeacherProps) => (
                    <TableRow
                    key={index++}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                        <TableCell component="th" scope="row">
                            <Typography variant="body2" sx={{ fontWeight: 500 }}>
                            {teacher.fullName}
                            </Typography>
                        </TableCell>
                        <TableCell sx={{ width: '200px' }}> 
                            {instituteBox(teacher.institute)}
                        </TableCell>
                        <TableCell>{teacher.description || '—'}</TableCell>
                        <TableCell>{formatDate(teacher.email)}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>;
}


export default TeachersTable;

import StudentImage from "../assets/student_photo.jpg";

import {
    Box, Paper,
    Typography, Avatar,
    Chip
} from "@mui/material";

import { deepPurple } from "@mui/material/colors";


interface ProfileProps {
    lastName: string;
    firstName: string;
    patronymic: string;
    // fullName: String;  // фио но одним полем (пошла я нахрен)
    age: number;  // Возраст
    birthDate: string;  // ДР
    // phoneNumber: string;  // Номер телефона (Считаем, что номер телефона всегда дан нам красиво)
    // emailAddress: string;  // Электронная почта
    // gradeLevel: string;  // Уровень подготовки: бакалавр, магистр...
    // study_mode: string;  // Очная / Заочная форма обучения (это не предусмотрено начальной моделью)
    // institute: string;  // Отделение / Институт
    // course: number;  // 1-й, 2-й, 3-й курс ОТКАЗ ОТ КУРСА)))))))))
    // program_code: string;  // Код направления (09.03.02, например)
    // group: string;  // Название группы

    path_to_photo?: string;  // То есть параметр необязательный
    loading?: boolean;
};


interface BadgeProps {
    text: String;
};


function CustomBadge(props: BadgeProps) {
    return  <Chip 
        label={props.text} 
        color="primary"
        sx={{bgcolor: "rgba(255,255,255,0.2)", color: "white"}} 
    />;
}


function Profile(profileData: ProfileProps) {

    return <Box sx={{p:3, maxWidth:1200, margin: "auto"}}>
        <Paper elevation={5} sx={{borderRadius:3, overflow:"hidden"}}></Paper>
        
        <Box sx={{
             width: {md: "500"}, // ширина ЛИНАР ГРАДИЕНТА 
             background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
             p: 4,
             color:"white"}}>

            <Box sx={{width: {xs:"100%", sm:"50%", md:"33%"}, p:{xs:1, md:3}, display:"flex"}}>
                <Avatar alt="student" 
                        src={StudentImage} 
                        sx={{bgcolor:deepPurple[500], width:200, height:200, fontSize:"500%"}}>
                            S
                </Avatar>
            </Box>
            <Box sx={{width: {xs: "100%", sm: "50%", md:"33%"}, p:{xs:1, md:3}, display:"table-row"}}>
                <Typography variant="h4" gutterBottom sx={{fontVariant: "full-width", mt:2}}> 
                    {`${profileData.lastName} ${profileData.firstName} ${profileData.patronymic}`}
                </Typography>
                    {/* курс */}
                    <Box sx={{display: "flex", gap:1, flexWrap:"wrap", mb:2, p:5}}>
                        {/* Вся инфа о пользователе в одном месте и красиво
                        <CustomBadge text={`${profileData.gradeLevel}, ${profileData.course} курс`}/>
                        <CustomBadge text={`Группа: ${profileData.group}`}/>
                        <CustomBadge text={`${profileData.emailAddress}`}/>
                        <CustomBadge text={`${profileData.emailAddress}`}/>
                        <CustomBadge text={`Направление подготовки: ${profileData.program_code}`}/>
                        <CustomBadge text={`Форма обучения: ${profileData.study_mode}`} /> */}
                        <CustomBadge text={`День рождения: ${profileData.birthDate}`}/>
                        <CustomBadge text={`${profileData.age} полных лет`}/>
                    </Box>
            </Box>
            

        </Box>
    </Box>;
}


export default Profile;

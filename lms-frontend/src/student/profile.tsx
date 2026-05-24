
import type { 
    // student_profile, 
    ProfileProps 
} from "./student_profile";
import { beautyPhoneNumber } from "./student_profile";
import StudentImage from "../assets/student_photo.jpg";

import React from 'react';
import {Box,
    Paper,
    // Grid,
    Typography,
    Avatar,
    // Card,
    // CardContent,
    Chip
} from '@mui/material';
// import { GroupAdd } from "@mui/icons-material";
// import {
//     Person as PersonIcon,
//     Cake as CakeIcon,
//     Phone as PhoneIcon,
//     Email as EmailIcon,
//     Home as HomeIcon,
//     School as SchoolIcon,
//     Class as ClassIcon,
//     Book as BookIcon,
//     Badge as BadgeIcon,
//     Groups as GroupsIcon,
// } from '@mui/icons-material';
import { deepPurple } from "@mui/material/colors";

const PersonBox = {


};



const Profile: React.FC<ProfileProps> = ({profileData}) => {
    return (
            <Box sx={{p:3, maxWidth:1200, margin: 'auto'}}>
                <Paper elevation={3} sx={{borderRadius:3, overflow:'hidden'}}></Paper>
                {/* ????????????????????????? */}
                <Box sx={{
                    // width: {md:'300'}, ширина ЛИНАР ГРАДИЕНТАА
                    background:'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    p:4,
                    // display:,
                    color:'white'
                }}>
                    <Box sx={{width: {xs:'100%', sm:'50%', md:'33%'}, p:{xs:1, md:3}, display:'flex'}} >
                        <Avatar alt='student' src={StudentImage} sx={{bgcolor:deepPurple[500],
                                width:200,
                                height:200,
                                fontSize:'500%'
                        }}>O</Avatar>
                    </Box>

                    <Box sx={{width: {xs: '100%', sm: '50%', md:'33%'}, p:{xs:1, md:3}, display:'table-row'}}>
                        <Typography variant="h4" gutterBottom sx={{fontVariant: 'full-width', mt:2}}> 
                            {profileData.full_name}
                        </Typography>
                        {/* курс */}
                        <Box sx={{display: 'flex', gap:1, flexWrap:'wrap', mb:2, p:5}}>
                            {/* чипы для профильДата */}
                            <Chip 
                                label={`${profileData.grade_level}, ${profileData.course} курс`} 
                                color='primary'
                                sx={{bgcolor: 'rgba(255,255,255,0.2)', color: 'white'}} 
                            />
                            <Chip 
                                label={`Группа: ${profileData.group}`}
                                // icon={<GroupsIcon/>}
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                            <Chip
                                label={`${profileData.email_adress}`}
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color:'white'}}
                            />
                            {/* ????????????????????????/ */}
                            <Chip
                                label={`${profileData.institute}`}
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}}
                                />
                            <Chip
                            label={`${beautyPhoneNumber(profileData.phone_number, profileData.country)}`}
                            sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}}
                            />
                            <Chip 
                                label={`Направление подготовки: ${profileData.program_code}`}
                                // icon={<GroupsIcon/>}
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                            <Chip 
                                label={`Форма обучения: ${profileData.study_mode}`}
                                // icon...
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                            <Chip 
                                label={`Группа: ${profileData.group}`}
                                // icon?
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                            <Chip 
                                label={`День рождения: ${profileData.b_date}`}
                                // icon...
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                            <Chip 
                                label={`Полных лет: ${profileData.age}`}
                                // icon...
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                            <Chip 
                                label={`Номер зачётной книжки: ${profileData.zachetka}`}
                                // icon...
                                sx={{bgcolor: 'rgba(255,255,255, 0.2)', color: 'white'}} 
                            />
                        </Box>
                    </Box>
                </Box>
            <Typography>
                    <Box>
                        <Typography>
                            hello there :D
                        </Typography>
                    </Box>
            </Typography>
            </Box>
    );
};

export default Profile
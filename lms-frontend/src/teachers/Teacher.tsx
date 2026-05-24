
export interface TeacherProps {
    fullName: string;
    institute: string;
    description: string;
    email: string;
};

// Мок заглушка
export const mockTeachers: TeacherProps[] = [
    {
        fullName: 'Перегуда Аркадий Иванович',
        institute: 'ОИКС',
        description: 'д.ф.-м.н., профессор',
        email: 'pereguda@iate.obninsk.ru',
    },
    {
        fullName: 'Нахабов Александр Владимирович',
        institute: 'ОЯФИТ',
        description: 'кандидат технических наук, доцент',
        email: 'AVNakhabov@mephi.ru',

    },
    // {
    //     fullName: 'name',
    //     institute: 'institute',
    //     description: 'regalis',
    //     email: 'email',
    // },
];

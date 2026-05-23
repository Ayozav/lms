export interface student_profile {
    full_name: String;
    age: Number;
    b_date: String;
    country: String;
    phone_number: Number; //?????????
    email_adress: String;
    home_adress: String;
    grade_level: String; // bachrlor or master
    study_mode: String; //full-time or another
    institute: String;
    course: Number;
    program_code: String;
    group: String;
    zachetka: String; //по-английски??
    path_to_photo: String;
}

export interface ProfileProps {
    profileData: student_profile;
    loading?:boolean;
}

export const beautyPhoneNumber = (phone: Number, country: String): string => {
    const phoneStr = String(phone);
    if (country == "ru") {
        return '+'+ phone;
    }
    return phoneStr; //to be continied?
}
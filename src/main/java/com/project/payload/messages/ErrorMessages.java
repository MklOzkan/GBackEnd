package com.project.payload.messages;

public class ErrorMessages {

    public static final String THERE_IS_NO_USER_WITH_THIS_Role ="There is no user with this role: %s" ;
    public static final String USER_ROLE_IS_NOT_FOUND = "User role is not found";
    public static final String USER_ID_IS_NOT_FOUND = "User id is not found %s";
    public static final String BUILTIN_USER_CAN_NOT_BE_DELETED ="BuiltIn user can not be deleted" ;
    public static final String ADMIN_CANNOT_DELETE_ADMIN ="Admin can not delete admin" ;
    public static final String BUILT_IN_USER_CAN_NOT_BE_UPDATED = "BuiltIn user can not be updated";
    public static final String THE_PASSWORDS_ARE_NOT_MATCHED = "The passwords are not matched";
    public static final String RESET_CODE_IS_NOT_FOUND ="Reset code is not found %s" ;
    public static final String THERE_IS_NO_USER_REGISTERED_WITH_THIS_EMAIL_ADRESS ="There is no user registered with this email adress" ;

    public static final String PASSWORD_HAS_ALREADY_TAKEN = "Password has already taken";
    public static final String USER_NOT_FOUND = "User not found";

    public static final String USER_IS_NOT_FOUND_BY_USERNAME ="Username is npt found" ;
    public static final String PASSWORD_IS_INCORRECT = "Sifre yanlis";
    public static final String THERE_IS_NO_USER_WITH_THIS_ROLE = "Bu role ile kayıtlı kullanıcı bulunamadı: %s";
    public static final String TIME_NOT_VALID_MESSAGE = "Zaman uygun değil";
    public static final String NOT_FOUND_ORDER = "Sipariş bulunamadı";
    public static final String USER_IS_NOT_ADMIN = "Kullanıcı yönetici değil";
    public static final String USER_IS_NOT_EMPLOYEE = "Kullanıcı çalışan değil";
    public static final String UNAUTHORIZED_USER = "Yetkisiz kullanıcı";


    //Order Error Messages
    public static final String ORDER_NOT_FOUND = "%s No'lu Sipariş bulunamadı";
    public static final String ORDER_ID_IS_NOT_FOUND = "Sipariş id bulunamadı %s";
    public static final String GASAN_NO_IS_ALREADY_EXIST = "%s No'lu Gasan numarası zaten mevcut";
    public static final String ORDER_NUMBER_IS_ALREADY_EXIST = "%s No'lu Sipariş numarası zaten mevcut";
    public static final String PRODUCTION_PROCESS_NOT_FOUND = "%s No'lu üretim süreci bulunamadı";
    public static final String TALASLI_IMALAT_NOT_FOUND = "%s operasyonu bulunamadı";

    public static final String TALASLI_IMALAT_OPERATION_TYPE_NOT_FOUND = "Belirtilen işlem tipinde talaşlı imalat bulunamadı: %s";
    public static final String POLISAJ_IMALAT_NOT_FOUND = "Polisaj imalat bulunamadı";
    public static final String KALITE_KONTROL_NOT_FOUND = "Kalite kontrol  aşaması bulunamadı";
    public static final String POLISAJ_NOT_FOUND = "Polisaj bulunamadı: %s";
    public static final String COMPLETED_QUANTITY_CANNOT_BE_GREATER_THAN_REMAINING_QUANTITY = "Tamamlanan miktar, kalan miktarı geçemez";

    private ErrorMessages(){}

}

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

    private ErrorMessages(){}

    public static final String THIS_PHONE_NUMBER_IS_ALREADY_TAKEN = "This phone %s number is already taken";

}

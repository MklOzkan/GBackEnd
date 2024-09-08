package com.project.payload.messages;

import org.springframework.http.HttpStatusCode;

public class SuccessMessages {


    public static final String THE_ROLES_HAS_ADDED ="The roles has added" ;
    public static final String THE_USER_HAS_BEEN_DELETED ="The user has been deleted" ;
    public static final String PASSWORD_UPDATED_SUCCESSFULLY ="Password updated successfully" ;
    public static final String PASSWORD_RESET_SUCCESSFULLY ="Password reset successfully" ;
    public static final String PASSWORD_SHOULD_NOT_MATCHED = "Kaydetmeye çalıstığınız şifre kayıtlı olan şifrelerden biriyle aynı olamaz";
    public static final String ORDER_DELETED = "Sipariş silindi";
    public static final String USER_SAVED = "Kullanıcı kaydedildi";
    public static final String ORDER_CREATED = "Sipariş oluşturuldu";
    public static final String ORDER_UPDATED = "Sipariş güncellendi";
    public static final String ORDER_FOUND = "Sipariş bulundu";
    public static final String ORDER_STARTED = "Sipariş başlatıldı";
    public static final String ORDER_COMPLETED = "Sipariş tamamlandı";
    public static final String ORDER_PAUSED = "Sipariş durduruldu ve beklemeye alındı";

    private SuccessMessages(){}
}

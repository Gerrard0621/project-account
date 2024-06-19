package com.ezen.ams.util;
/*
 * 데이터 유효성 검증을 위한 유틸리티 클래스
 */
public class Validator {
    /**
     * 데이터 입력 여부 검증
     * @param value 검증하고자 하는 문자열
     * @return 값이 있을 경우 true, 없을경우 false 이다.
     */
    //아이디 입력창에 입력했는지 안했는지
    public static boolean isText(String value){
        if(value != null && value.trim().length() !=0){
            return true;
        }
        return false;
    }
    //아이디에 숫자랑 알파벳만 들어오게하기
    public static boolean isId(String inputId){
        if(inputId == null){
            return false;
        }
        for (int i = 0; i < inputId.length(); i++) {
            char ch = inputId.charAt(i);
            if(!Character.isAlphabetic(ch) && !Character.isDigit(ch)){
                return false;
            }
        } return true;
    }
    //숫자만 들어오게하기
    public static boolean isNumber(String value){
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if(!Character.isDigit(ch)){
                return false;
            }
        } return true;
    }
}

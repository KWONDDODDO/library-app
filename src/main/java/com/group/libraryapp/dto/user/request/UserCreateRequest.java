package com.group.libraryapp.dto.user.request;

public class UserCreateRequest {

    private String name;    //null불가능
    private Integer age;    //int는 null표현 불가능, But Integer 는 null 표현 가능

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}

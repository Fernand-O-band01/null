package com.example.demo.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("null_activation_account")
    ;
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }

}

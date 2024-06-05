package org.javaacademy.party.entity;

import lombok.Data;

@Data
public class Guest {
    private Long id;
    private String name;
    private String email;
    private Long phone;
}

package com.taskhub.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserResponseDTO {

    private Long id;
    private String username;
    private String roles;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String username, String roles){
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}

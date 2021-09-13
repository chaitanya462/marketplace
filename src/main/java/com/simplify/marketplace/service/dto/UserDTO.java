package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.User;
import lombok.Data;

/**
 * A DTO representing a user, with only the public attributes.
 */

@Data
public class UserDTO {

    private Long id;

    private String login;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }
}

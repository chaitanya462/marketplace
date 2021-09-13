package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.OtpStatus;
import com.simplify.marketplace.domain.enumeration.OtpType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.Otp} entity.
 */
@Data
public class OtpDTO implements Serializable {

    private Long id;

    private String contextId;

    private Integer otp;

    private String email;

    private Boolean isActive;

    private Integer phone;

    private OtpType type;

    private LocalDate expiryTime;

    private OtpStatus status;

    private String createdBy;

    private LocalDate createdAt;
}

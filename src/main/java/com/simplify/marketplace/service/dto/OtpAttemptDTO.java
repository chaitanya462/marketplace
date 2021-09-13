package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.OtpStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.OtpAttempt} entity.
 */
@Data
public class OtpAttemptDTO implements Serializable {

    private Long id;

    private String contextId;

    private Integer otp;

    private Boolean isActive;

    private OtpStatus status;

    private String ip;

    private String coookie;

    private String createdBy;

    private LocalDate createdAt;
}

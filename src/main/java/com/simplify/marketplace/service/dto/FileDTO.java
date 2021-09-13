package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.FileFormat;
import com.simplify.marketplace.domain.enumeration.FileType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the {@link com.simplify.marketplace.domain.File} entity.
 */
@Data
public class FileDTO implements Serializable {

    private Long id;

    private String path;

    private FileFormat fileformat;

    private FileType filetype;

    private String tag;

    private Boolean isDefault;

    private Boolean isResume;

    private Boolean isProfilePic;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;
}

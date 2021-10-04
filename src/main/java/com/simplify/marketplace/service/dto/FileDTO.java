package com.simplify.marketplace.service.dto;

import com.simplify.marketplace.domain.enumeration.FileFormat;
import com.simplify.marketplace.domain.enumeration.FileType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Objects;
import javax.persistence.Lob;
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

    @Lob
    private byte[] filename;

    private String filenameContentType;
    private String tag;

    private Boolean isDefault;

    private Boolean isResume;

    private Boolean isProfilePic;

    private WorkerDTO worker;

    private String createdBy;

    private LocalDate createdAt;

    private String updatedBy;

    private LocalDate updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileFormat getFileformat() {
        return fileformat;
    }

    public void setFileformat(FileFormat fileformat) {
        this.fileformat = fileformat;
    }

    public FileType getFiletype() {
        return filetype;
    }

    public void setFiletype(FileType filetype) {
        this.filetype = filetype;
    }

    public byte[] getFilename() {
        return filename;
    }

    public void setFilename(byte[] filename) {
        this.filename = filename;
    }

    public String getFilenameContentType() {
        return filenameContentType;
    }

    public void setFilenameContentType(String filenameContentType) {
        this.filenameContentType = filenameContentType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsResume() {
        return isResume;
    }

    public void setIsResume(Boolean isResume) {
        this.isResume = isResume;
    }

    public Boolean getIsProfilePic() {
        return isProfilePic;
    }

    public void setIsProfilePic(Boolean isProfilePic) {
        this.isProfilePic = isProfilePic;
    }

    public WorkerDTO getWorker() {
        return worker;
    }

    public void setWorker(WorkerDTO worker) {
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDTO)) {
            return false;
        }

        FileDTO fileDTO = (FileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", fileformat='" + getFileformat() + "'" +
            ", filetype='" + getFiletype() + "'" +
            ", filename='" + getFilename() + "'" +
            ", tag='" + getTag() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", isResume='" + getIsResume() + "'" +
            ", isProfilePic='" + getIsProfilePic() + "'" +
            ", worker=" + getWorker() +
            "}";
    }
}

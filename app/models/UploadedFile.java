package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.data.validation.Constraints;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class UploadedFile {
    @Id
    private String path;
    @ManyToOne
    private User uploadedBy;
    @Constraints.Required
    private FileType fileType;

    @Transactional
    public static void update(UploadedFile file) {
        Ebean.update(file);
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(final FileType fileType) {
        this.fileType = fileType;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public enum FileType {
        CLUBIMAGE
    }
}
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UploadedFile {

    @Id
    @GeneratedValue
    private String id;
    @ManyToOne
    private User uploadedBy;

    private String fileName;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }
}
package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class UploadedFile extends Model {
    public static Finder<String, UploadedFile> find = new Finder<>(String.class, UploadedFile.class);
    @Id
    public String path;
    @ManyToOne
    public User uploadedBy;
    @Constraints.Required
    public FileType fileType;

    @Transactional
    public static void update(UploadedFile file) {
        Ebean.update(file);
    }

    public enum FileType {
        CLUBIMAGE
    }
}
package models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import plugins.S3Plugin;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Entity
public class UploadedFile {

    @Id
    private UUID id;

    private String name;
    @Transient
    private File file;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @JsonGetter
    public URL getUrl() throws MalformedURLException {
        return new URL("https://s3.amazonaws.com/" + S3Plugin.s3Bucket + "/" + getActualFileName());
    }

    public String getActualFileName() {
        return id + "/" + name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }
}
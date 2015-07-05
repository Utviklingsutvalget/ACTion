package services;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avaje.ebean.Ebean;
import models.UploadedFile;
import play.Logger;
import plugins.S3Plugin;

import java.util.UUID;

public class FileService {

    public void save(UploadedFile file) {
        if (S3Plugin.amazonS3 == null) {
            Logger.error("Could not save because amazonS3 was null");
            throw new RuntimeException("Could not save");
        }
        else {

            Ebean.save(file); // assigns an id

            PutObjectRequest putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, file.getActualFileName(), file.getFile());
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
        }
    }

    public void delete(UploadedFile file) {
        if (S3Plugin.amazonS3 == null) {
            Logger.error("Could not delete because amazonS3 was null");
            throw new RuntimeException("Could not delete");
        }
        else {
            S3Plugin.amazonS3.deleteObject(S3Plugin.s3Bucket, file.getActualFileName());
            Ebean.delete(file);
        }
    }

    public UploadedFile getUploadedFileFromStringId(final String uploadedFileId) {
        Logger.info("Querying for uploaded file: " + uploadedFileId);
        return Ebean.find(UploadedFile.class).setId(UUID.fromString(uploadedFileId)).findUnique();
    }
}

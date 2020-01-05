package org.DevNex.ChatApp.StorageServer;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import org.DevNex.ChatApp.Utils.Helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

public class SpaceHelper
{

    private static final String ProfileImagePath = "/Images/ProfileImages/";
    private static final String RoomImagePath = "/Images/RoomImages/";
    private static final String MessageImagePath = "/Images/MessageImages/";
    private static final String Attachments = "/Files/Attachments/";

    private String BucketName;
    private String Endpoint;
    private String Region;
    private String AccessKey;
    private String SecretKey;

    public SpaceHelper (String BucketName, String Endpoint, String Region, String AccessKey, String SecretKey)
    {
        this.BucketName = BucketName;
        this.Endpoint = Endpoint;
        this.Region = Region;
        this.AccessKey = AccessKey;
        this.SecretKey = SecretKey;
    }

    public String UploadProfileImage (String EncodedImage)
    {
        AmazonS3 Space = GetSpaceConnection ();

        byte[] ImageBytes = Base64.getDecoder ().decode (EncodedImage);
        InputStream Stream = new ByteArrayInputStream (ImageBytes);
        ObjectMetadata Meta = new ObjectMetadata ();
        Meta.setContentLength (ImageBytes.length);
        Meta.setContentType ("image/jpg");

        String Path = MakeImagePath (ProfileImagePath);
        Space.putObject (BucketName, Path, Stream, Meta);

        return Space.getUrl (BucketName, Path).toString ();
    }

    public String UploadRoomImage (String EncodedImage)
    {
        AmazonS3 Space = GetSpaceConnection ();

        byte[] ImageBytes = Base64.getDecoder ().decode (EncodedImage);
        InputStream Stream = new ByteArrayInputStream (ImageBytes);
        ObjectMetadata Meta = new ObjectMetadata ();
        Meta.setContentLength (ImageBytes.length);
        Meta.setContentType ("image/jpg");

        String Path = MakeImagePath (RoomImagePath);
        Space.putObject (BucketName, Path, Stream, Meta);

        return Space.getUrl (BucketName, Path).toString ();
    }

    public String UploadMessageImage (String EncodedImage)
    {
        AmazonS3 Space = GetSpaceConnection ();

        byte[] ImageBytes = Base64.getDecoder ().decode (EncodedImage);
        InputStream Stream = new ByteArrayInputStream (ImageBytes);
        ObjectMetadata Meta = new ObjectMetadata ();
        Meta.setContentLength (ImageBytes.length);
        Meta.setContentType ("image/jpg");

        String Path = MakeImagePath (MessageImagePath);
        Space.putObject (BucketName, Path, Stream, Meta);

        return Space.getUrl (BucketName, Path).toString ();
    }

    private AmazonS3 GetSpaceConnection ()
    {
        AWSCredentialsProvider Provider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(AccessKey, SecretKey)
        );

        AmazonS3 Space = AmazonS3ClientBuilder
                .standard ()
                .withCredentials (Provider)
                .withEndpointConfiguration (
                        new AwsClientBuilder.EndpointConfiguration (Endpoint, Region)
                )
                .build ();

        return Space;
    }

    private static String MakeImagePath (String Path)
    {
        return Path + Helper.GenerateToken (10) + ".jpg";
    }

}

package com.mylstech.product.impl;

import com.mylstech.product.config.FileStorageProperties;
import com.mylstech.product.exception.DirectoryCreationException;
import com.mylstech.product.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get ( fileStorageProperties.getUploadDir ( ) )
                .toAbsolutePath ( ).normalize ( );

        try {
            Files.createDirectories ( this.fileStorageLocation );
        }
        catch ( Exception ex ) {
            throw new DirectoryCreationException ( "Could not create the directory where the uploaded files will be stored.", ex );
        }
    }


    @PostConstruct
    public void init() {
        // Check if WebP writer is available
        boolean webpWriterAvailable = false;
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType ( "image/webp" );
        if ( writers.hasNext ( ) ) {
            webpWriterAvailable = true;
        }

        if ( ! webpWriterAvailable ) {
            log.warn ( "WebP image writer not available. Images will not be converted to WebP format." );
            // You could try to register the WebP writer here if needed
        } else {
            log.info ( "WebP image writer is available. Images will be converted to WebP format." );
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String originalFilename = StringUtils.cleanPath ( Objects.requireNonNull ( file.getOriginalFilename ( ) ) );

        // Generate a unique file name to prevent conflicts
        String fileExtension = "";
        if ( originalFilename.contains ( "." ) ) {
            fileExtension = originalFilename.substring ( originalFilename.lastIndexOf ( "." ) );
        }

        // Check both file extension and content type
        boolean isConvertibleByExtension = isConvertibleImage ( fileExtension );
        boolean isConvertibleByContentType = isConvertibleContentType ( file.getContentType ( ) );

        log.info ( "File: {}, Extension: {}, Content-Type: {}, Convertible by extension: {}, Convertible by content type: {}",
                originalFilename, fileExtension, file.getContentType ( ), isConvertibleByExtension, isConvertibleByContentType );

        // Convert if either extension or content type indicates it's a convertible image
        if ( isConvertibleByExtension || isConvertibleByContentType ) {
            try {
                // Convert to WebP and store
                return storeWebPImage ( file );
            }
            catch ( IOException e ) {
                log.warn ( "Failed to convert image to WebP: {}", e.getMessage ( ) );
                // If conversion fails, fall back to original format
                return storeOriginalFile ( file, originalFilename, fileExtension );
            }
        } else {
            // For non-image files or unsupported image formats, store as is
            return storeOriginalFile ( file, originalFilename, fileExtension );
        }
    }

    private String storeOriginalFile(MultipartFile file, String originalFilename, String fileExtension) {
        String uniqueFilename = UUID.randomUUID ( ).toString ( ) + fileExtension;

        try {
            // Check if the file's name contains invalid characters
            if ( originalFilename.contains ( ".." ) ) {
                throw new InvalidFileNameException ( originalFilename, "Sorry! Filename contains invalid path sequence " );
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve ( uniqueFilename );
            Files.copy ( file.getInputStream ( ), targetLocation, StandardCopyOption.REPLACE_EXISTING );

            return uniqueFilename;
        }
        catch ( IOException ex ) {
            throw new DirectoryCreationException ( "Could not store file " + originalFilename + ". Please try again!", ex );
        }
    }

    private String storeWebPImage(MultipartFile file) throws IOException {
        // Generate a unique filename with WebP extension
        String uniqueFilename = UUID.randomUUID ( ).toString ( ) + ".webp";

        // Convert the image to WebP format
        byte[] webpData = convertToWebP ( file );

        // Save the WebP image
        Path targetLocation = this.fileStorageLocation.resolve ( uniqueFilename );
        Files.write ( targetLocation, webpData );

        return uniqueFilename;
    }

    private byte[] convertToWebP(MultipartFile file) throws IOException {
        // Read the original image
        BufferedImage originalImage = ImageIO.read ( file.getInputStream ( ) );
        if ( originalImage == null ) {
            throw new IOException ( "Failed to read image file" );
        }

        // Create output stream for WebP image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream ( );

        // Use ImageIO with WebP writer
        // Note: This requires additional libraries like com.twelvemonkeys.imageio:imageio-webp
        // or org.sejda.imageio:webp-imageio
        if ( ! ImageIO.write ( originalImage, "webp", outputStream ) ) {
            throw new IOException ( "No appropriate writer found for WebP format" );
        }

        return outputStream.toByteArray ( );
    }

    private boolean isConvertibleImage(String fileExtension) {
        if ( fileExtension == null || fileExtension.isEmpty ( ) ) {
            return false;
        }

        String ext = fileExtension.toLowerCase ( );
        // Ensure the extension starts with a dot
        if ( ! ext.startsWith ( "." ) ) {
            ext = "." + ext;
        }

        // Common image formats that can be converted to WebP
        return ext.equals ( ".jpg" ) || ext.equals ( ".jpeg" ) ||
                ext.equals ( ".png" ) || ext.equals ( ".bmp" ) ||
                ext.equals ( ".gif" );
    }

    private boolean isConvertibleContentType(String contentType) {
        if ( contentType == null || contentType.isEmpty ( ) ) {
            return false;
        }

        // Common image content types that can be converted to WebP
        return contentType.equals ( "image/jpeg" ) ||
                contentType.equals ( "image/png" ) ||
                contentType.equals ( "image/bmp" ) ||
                contentType.equals ( "image/gif" );
    }

    @Override
    public Path getFilePath(String fileName) {
        return this.fileStorageLocation.resolve ( fileName );
    }
}
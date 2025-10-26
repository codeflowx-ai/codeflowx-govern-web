package com.suinsit.leka.service;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
 
@Service
@Slf4j
public class FileUploadService {
 
    private final Path uploadDir;
 
    public FileUploadService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = Path.of(uploadDir);
    }
 
    public Mono<String> handleFileUpload(FilePart filePart) {
        Path destinationFile = uploadDir.resolve(filePart.filename());
        return DataBufferUtils.write(
            filePart.content(),
            destinationFile,
            StandardOpenOption.CREATE
        )
        .then(Mono.just(destinationFile.toString()))
        .doOnError(error -> log.error("Error handling file upload", error))
        .onErrorResume(error -> Mono.error(
            new FileUploadException("Failed to upload file: " + error.getMessage())
        ));
    }
 
    public Mono<Void> handleLargeDataStream(Flux<DataBuffer> dataStream) {
        return DataBufferUtils.join(dataStream)
            .map(buffer -> {
                // Procesar el buffer de manera segura
                try {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);
                    return bytes;
                } catch (Exception e) {
                    throw new DataProcessingException("Error processing data stream", e);
                }
            })
            .then();
    }
}
 
class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }
}
 
class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

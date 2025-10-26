package com.suinsit.leka.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;

import com.suinsit.leka.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class FileUploadController {
 
	@Autowired
    private final FileUploadService fileUploadService;
 
    @PostMapping("/file")
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file") FilePart filePart) {
        return fileUploadService.handleFileUpload(filePart)
            .map(path -> ResponseEntity.ok("File uploaded successfully: " + path))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.internalServerError().body("Upload failed: " + error.getMessage())
            ));
    }
 
    @PostMapping("/stream")
    public Mono<ResponseEntity<Void>> handleDataStream(@RequestBody Flux<DataBuffer> dataStream) {
        return fileUploadService.handleLargeDataStream(dataStream)
            .then(Mono.just(ResponseEntity.ok().<Void>build()))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.internalServerError().<Void>build()
            ));
    }
}

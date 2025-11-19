package ru.hogwarts.school.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.Service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile file) throws IOException {
        avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok("Avatar uploaded successfully");
    }

    @GetMapping("/from-db/{studentId}")
    public ResponseEntity<Resource> getAvatarFromDB(@PathVariable Long studentId) {
        var avatar = avatarService.getAvatarFromDB(studentId);
        byte[] data = avatar.getData();
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + avatar.getFilePath() + "\"")
                .contentLength(data.length)
                .body(resource);
    }

    @GetMapping("/from-disk/{studentId}")
    public ResponseEntity<Resource> getAvatarFromDisk(@PathVariable Long studentId) throws IOException {
        byte[] data = avatarService.getAvatarFromDisk(studentId);
        String contentType = "image/jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
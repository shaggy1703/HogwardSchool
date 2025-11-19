package ru.hogwarts.school.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    private final Path avatarDir = Paths.get("avatars");

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
        try {
            Files.createDirectories(avatarDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create avatar directory", e);
        }
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.read(studentId);

        if (student.getAvatar() != null) {
            Avatar oldAvatar = student.getAvatar();
            Path oldFilePath = avatarDir.resolve(oldAvatar.getFilePath());
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                System.err.println("Failed to delete old avatar file: " + oldFilePath);
            }
            avatarRepository.delete(oldAvatar);
            student.setAvatar(null);
        }

        String fileName = studentId + "_" + file.getOriginalFilename();
        Path filePath = avatarDir.resolve(fileName);
        file.transferTo(filePath.toFile());

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        student.setAvatar(avatar);
        avatarRepository.save(avatar);
    }

    public Avatar getAvatarFromDB(Long studentId) {
        Student student = studentService.read(studentId);
        Avatar avatar = student.getAvatar();
        if (avatar == null) {
            throw new RuntimeException("Avatar not found for student ID: " + studentId);
        }
        return avatar;
    }

    public byte[] getAvatarFromDisk(Long studentId) throws IOException {
        Student student = studentService.read(studentId);
        Avatar avatar = student.getAvatar();
        if (avatar == null) {
            throw new RuntimeException("Avatar not found for student ID: " + studentId);
        }
        Path filePath = Paths.get(avatar.getFilePath());
        return Files.readAllBytes(filePath);
    }
}
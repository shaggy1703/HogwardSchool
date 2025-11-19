package ru.hogwarts.school.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "avatars")
public class Avatar {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return id == avatar.id && fileSize == avatar.fileSize && Objects.equals(filePath, avatar.filePath) && Objects.equals(mediaType, avatar.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, mediaType);
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", data length=" + (data != null ? data.length : 0) +
                ", student=" + student +
                '}';
    }
}
package com.fullcycle.catalogo.admin.infrastructure.video.persistence.media;

import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Table(name = "videos_video_media")
@Entity(name = "AudioVideoMedia")
public class AudioVideoMediaJpaEntity {
    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private MediaStatus status;

    @Deprecated
    AudioVideoMediaJpaEntity() {}

    private AudioVideoMediaJpaEntity(
        final String id,
        final String name,
        final String filePath,
        final String encodedPath,
        final MediaStatus status
    ) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia media) {
        return new AudioVideoMediaJpaEntity(
            media.checksum(),
            media.name(),
            media.rawLocation(),
            media.encodedLocation(),
            media.status()
        );
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
            id,
            name,
            filePath,
            encodedPath,
            status
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }
}

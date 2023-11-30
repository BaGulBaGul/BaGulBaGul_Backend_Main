package com.BaGulBaGul.BaGulBaGul.global.upload;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class S3TempResource {
    @Id
    @Column(name = "resource_key")
    private String key;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;
}

package com.BaGulBaGul.BaGulBaGul.domain.upload;

import com.BaGulBaGul.BaGulBaGul.domain.upload.constant.StorageVendor;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;

    @Setter
    @Column(name = "resource_key")
    private String key;

    @Setter
    @Column(name = "storage_vendor")
    @Enumerated(value = EnumType.STRING)
    private StorageVendor storageVendor;

    @Setter
    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Builder
    public Resource(String key, StorageVendor storageVendor, LocalDateTime uploadTime) {
        this.key = key;
        this.storageVendor = storageVendor;
        this.uploadTime = uploadTime;
    }
}

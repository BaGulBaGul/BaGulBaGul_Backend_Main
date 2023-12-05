package com.BaGulBaGul.BaGulBaGul.global.upload;

import com.BaGulBaGul.BaGulBaGul.global.upload.constant.StorageVendor;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Resource {
    @Id
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "resource_key")
    private String key;

    @Column(name = "storage_vendor")
    @Enumerated(value = EnumType.STRING)
    private StorageVendor storageVendor;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;
}

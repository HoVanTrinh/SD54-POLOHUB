package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nationalized
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String link;
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}

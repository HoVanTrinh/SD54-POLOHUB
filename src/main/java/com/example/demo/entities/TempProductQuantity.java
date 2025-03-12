package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"productDetailId", "createId"}))
@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempProductQuantity {
    @Id
    private TempProductQuantityId id;
    @NotNull
    @Positive
    private Integer quantity;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class TempProductQuantityId implements Serializable {
        private Long createId;
        private Long productDetailId;

    }
}

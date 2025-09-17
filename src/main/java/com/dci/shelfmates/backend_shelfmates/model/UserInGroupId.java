package com.dci.shelfmates.backend_shelfmates.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInGroupId implements Serializable {
    private Long groupId;
    private Long userId;
}

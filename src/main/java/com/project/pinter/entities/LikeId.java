package com.project.pinter.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class LikeId implements Serializable {

    private UUID user;
    private UUID image;

    public LikeId() {}

    public LikeId(UUID user, UUID image) {
        this.user = user;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(user, likeId.user) &&
                Objects.equals(image, likeId.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, image);
    }
}
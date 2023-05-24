package dev.kmandalas.producer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class EventEntity {

    @Id
    @NonNull
    private Long id;

    @NonNull
    private String requestId;

    @NonNull
    private String status;

    @NonNull
    private LocalDateTime dateSent;

    @NonNull
    private LocalDateTime dateStored;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventEntity that = (EventEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

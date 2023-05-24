package dev.kmandalas.consumer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events_processed")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventProcessedEntity {

    @Id
    private String requestId;

    private String status;

    private LocalDateTime dateSent;

    private LocalDateTime dateProcessed;

    private String processedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventProcessedEntity that = (EventProcessedEntity) o;
        return requestId != null && Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

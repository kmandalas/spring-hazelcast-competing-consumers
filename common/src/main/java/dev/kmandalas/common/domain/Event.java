package dev.kmandalas.common.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Event implements Serializable {

    private UUID requestId;
    private Status status;
    private LocalDateTime dateSent;

}
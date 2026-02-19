package pl.jakubholik90.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public class StatusChangeEvent {

    private final Long id;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final DocumentStatus fromStatus;
    private final DocumentStatus toStatus;
    private final RecipientType fromRecipient;
    private final RecipientType toRecipient;
    private final String changedBy;
    private final String reason;

    // only getters
    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public DocumentStatus getFromStatus() {
        return fromStatus;
    }

    public DocumentStatus getToStatus() {
        return toStatus;
    }

    public RecipientType getFromRecipient() {
        return fromRecipient;
    }

    public RecipientType getToRecipient() {
        return toRecipient;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public String getReason() {
        return reason;
    }

    //hash and equals override
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StatusChangeEvent that)) return false;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override // override toString() for comparing
    public String toString() {
        return "StatusChangeEvent{" +
                "id=" + id +
                '}';
    }
}

package pl.jakubholik90.adapter.out.persistence.jpa;

import lombok.Builder;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.StatusChangeEventEntity;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.model.StatusChangeEvent;

import java.time.LocalDateTime;

public class StatusChangeEventMapper {

    public static StatusChangeEventEntity mapToEntity(StatusChangeEvent statusChangeEvent) {
        StatusChangeEventEntity returnEntity = new StatusChangeEventEntity();
        Long id = statusChangeEvent.getId();
        LocalDateTime timestamp = statusChangeEvent.getTimestamp();
        DocumentStatus fromStatus = statusChangeEvent.getFromStatus();
        DocumentStatus toStatus = statusChangeEvent.getToStatus();
        RecipientType fromRecipient = statusChangeEvent.getFromRecipient();
        RecipientType toRecipient = statusChangeEvent.getToRecipient();
        String changedBy = statusChangeEvent.getChangedBy();
        String reason = statusChangeEvent.getReason();

        if (id!=null) {
            returnEntity.setId(id);
        }
        returnEntity.setTimestamp(timestamp);
        returnEntity.setFromStatus(fromStatus);
        returnEntity.setToStatus(toStatus);
        returnEntity.setFromRecipient(fromRecipient);
        returnEntity.setToRecipient(toRecipient);
        returnEntity.setChangedBy(changedBy);
        returnEntity.setReason(reason);

        return returnEntity;
    }

    public static StatusChangeEvent mapToEvent(StatusChangeEventEntity statusChangeEventEntity) {
        StatusChangeEvent returnEvent = StatusChangeEvent.builder()
                .id(statusChangeEventEntity.getId())
                .timestamp(statusChangeEventEntity.getTimestamp())
                .fromStatus(statusChangeEventEntity.getFromStatus())
                .toStatus(statusChangeEventEntity.getToStatus())
                .fromRecipient(statusChangeEventEntity.getFromRecipient())
                .toRecipient(statusChangeEventEntity.getFromRecipient())
                .changedBy(statusChangeEventEntity.getChangedBy())
                .reason(statusChangeEventEntity.getReason())
                .build();

        return returnEvent;
    }
}

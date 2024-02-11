package com.renew.sw.mentoring.global.debug.model;

import com.renew.sw.mentoring.global.debug.model.dto.ErrorResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorLog {
    public final ErrorResponseDto info;
    public final String errorLog;

    @Override
    public String toString() {
        return String.format("Tracking ID: %s\n" +
                        "Timestamp: %s\n" +
                        "Status: %s\n" +
                        "Code: %s\n" +
                        "Message: %s\n" +
                        "%s",
                info.getTrackingId(), info.getTimestamp(), info.getStatus(),
                info.getCode(), info.getMessage(), errorLog);
    }
}

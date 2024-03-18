package com.renew.sw.mentoring.domain.post.model.entity.dto.request;

import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class RequestCreateMissionBoardDto extends RequestCreateGenericPostDto<MissionBoard> {

    private final Long missionId;
    private final Boolean isBonusMissionSuccessful;
    public RequestCreateMissionBoardDto(@NotBlank String title,
                                        @NotBlank String body,
                                        @NotBlank Long missionId,
                                        Boolean isBonusMissionSuccessful,
                                        List<MultipartFile> images) {
        super(title, body, images);
        this.missionId = missionId;
        this.isBonusMissionSuccessful = isBonusMissionSuccessful;
    }

    public MissionBoard toEntity(User user) {
        return MissionBoard.builder()
                .user(user)
                .title(getTitle())
                .body(getBody())
                .missionId(missionId)
                .isBonusMissionSuccessful(isBonusMissionSuccessful != null && isBonusMissionSuccessful)
                .registerStatus(RegisterStatus.IN_PROGRESS)
                .build();
    }
}

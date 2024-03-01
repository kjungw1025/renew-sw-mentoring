package com.renew.sw.mentoring.domain.excel;

import com.renew.sw.mentoring.domain.team.exception.TeamNotFoundException;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamParser {

    private final UserService userService;

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 팀 엑셀 파일 파싱
     *
     * <p>팀 명단은 sheet 1개로 이루어져 있으며 조, 이름, 학번, 조장확인 으로 구성되어 있다.</p>
     */
    public void parseTeam(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getPhysicalNumberOfRows();
            int teamNumber = 0;

            for (int r = 1; r < rows; r++) {
                String studentName = "";
                String studentId = "";
                String password = "12345678";
                boolean isMentor = false;

                XSSFRow row = sheet.getRow(r);

                if (row != null) {

                    for (int c = 0; c < 4; c++) {
                        XSSFCell cell = row.getCell(c);

                        // 조 확인 및 생성
                        if (c == 0 && cell.getStringCellValue().contains("조")){
                            ++teamNumber;
                            String teamName = makeTeamName(teamNumber);
                            if (findTeam(teamName).isEmpty()) {
                                Team team = Team.builder()
                                        .teamName(teamName)
                                        .build();
                                teamRepository.save(team);
                            }
                        // 이름
                        } else if (c == 1 && !cell.getStringCellValue().isEmpty()) {
                            studentName = cell.getStringCellValue();
                        // 학번
                        } else if (c == 2) {
                            studentId = String.valueOf((int) cell.getNumericCellValue());
                        // 조장 확인
                        } else if (c == 3 && cell.getStringCellValue().equals("O")) {
                            isMentor = true;
                        }

                        if (c == 3) {
                            User user = User.builder()
                                    .team(findTeam(makeTeamName(teamNumber)).orElseThrow(TeamNotFoundException::new))
                                    .name(studentName)
                                    .studentId(studentId)
                                    .nickname(userService.createRandomNickname())
                                    .userRole(isMentor ? UserRole.MENTOR : UserRole.MENTEE)
                                    .password(passwordEncoder.encode(password))
                                    .build();

                            userRepository.save(user);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("TeamParser.parseExcel() : {}", e.getMessage());
        }
    }

    private String makeTeamName(int teamNumber) {
        return "소웨 " + teamNumber + "조";
    }

    public Optional<Team> findTeam(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }
}

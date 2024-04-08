package com.renew.sw.mentoring.domain.excel;

import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.MissionStatus;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.mission.repository.BonusMissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionParser {

    private final MissionRepository missionRepository;
    private final BonusMissionRepository bonusMissionRepository;

    public void parseMission(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            for (int s = 1; s <= 6; s ++) {
                XSSFSheet sheet = workbook.getSheetAt(s);
                String difficulty = checkMissionDifficulty(s);
                int point = checkMissionPoint(s);

                int rows = getRows(s);

                for (int r = 1; r < rows; r++ ){
                    String name = "";
                    String description = "";
                    String bonusMission = "";

                    XSSFRow row = sheet.getRow(r);

                    if (row != null) {
                        for (int c = 1; c < 4; c++) {
                            XSSFCell cell = row.getCell(c);

                            //미션 이름
                            if(c == 1 && cell.getStringCellValue().length() >= 2) {
                                name = cell.getStringCellValue().trim();
                            //미션 설명
                            } else if (c == 2 && cell.getStringCellValue().length() >= 2) {
                                description = cell.getStringCellValue().trim();
                            }
                            // 보너스 미션
                            else if (c == 3 && !cell.getStringCellValue().isEmpty() && cell.getStringCellValue().length() >= 2) {
                                bonusMission = cell.getStringCellValue().trim();
                            }
                        }
                    }
                    if (name.length() >= 2) {
                        Mission mission = Mission.builder()
                                .name(name)
                                .description(description)
                                .point(point)
                                .difficulty(Difficulty.of(difficulty))
                                .missionStatus(MissionStatus.MAIN)
                                .build();
                        missionRepository.save(mission);

                        if (!bonusMission.isEmpty()) {
                            if(bonusMission.contains("월미도")) {
                                String[] split = bonusMission.split("\\+");
                                for(int i = 0; i < 3; i++) {
                                    String bonusName = split[i].trim();
                                    String bonusPointSplit = split[i + 1].trim();
                                    int bonusPoint;

                                    if (split[i+1].trim().contains("점")) {
                                        bonusPoint = Integer.parseInt(bonusPointSplit.substring(0, split[i+1].trim().lastIndexOf("점")));
                                    } else {
                                        bonusPoint = Integer.parseInt(bonusPointSplit);
                                    }
                                    BonusMission bm = BonusMission.builder()
                                            .name(bonusName)
                                            .description("")
                                            .point(bonusPoint)
                                            .mission(mission)
                                            .build();
                                    bonusMissionRepository.save(bm);
                                }
                            } else {
                                String[] split = bonusMission.split("\\+");
                                String bonusName = split[0].trim();
                                String bonusPointSplit = split[1].trim();
                                int bonusPoint;

                                if (split[1].trim().contains("점")) {
                                    bonusPoint = Integer.parseInt(bonusPointSplit.substring(0, split[1].trim().lastIndexOf("점")));
                                } else {
                                    bonusPoint = Integer.parseInt(bonusPointSplit);
                                }
                                BonusMission bm = BonusMission.builder()
                                        .name(bonusName)
                                        .description("")
                                        .point(bonusPoint)
                                        .mission(mission)
                                        .build();
                                bonusMissionRepository.save(bm);
                            }
                        }
                    }
                    if (name.length() >= 2) {
                        log.info("Name : {}, description : {}, bonusMission : {}, difficulty : {}, point : {}", name, description, bonusMission, difficulty, point);
                    }
                }
            }

        } catch (Exception e) {
            log.info("MissionParser.parseExcel() : {}", e.getMessage());
            log.info("StackTrace : {}", e.getStackTrace());
        }
    }

    private int getRows(int s) {
        return switch (s) {
            case 1 -> 19;
            case 2 -> 57;
            case 3 -> 75;
            case 4 -> 21;
            case 5 -> 23;
            case 6 -> 6;
            default -> 0;
        };
    }

    private String checkMissionDifficulty(int sheetNumber) {
        return switch (sheetNumber) {
            case 1 -> "VERY_EASY";
            case 2 -> "EASY";
            case 3 -> "NORMAL";
            case 4 -> "NORMAL_HARD";
            case 5 -> "HARD";
            case 6 -> "VERY_HARD";
            default -> null;
        };
    }

    private int checkMissionPoint(int sheetNumber) {
        return switch (sheetNumber) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 30;
            case 4 -> 50;
            case 5 -> 70;
            case 6 -> 300;
            default -> 0;
        };
    }
}

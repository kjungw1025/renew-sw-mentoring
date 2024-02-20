package com.renew.sw.mentoring.domain.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionParser {

    public void parseMission(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(1);

            int rows = sheet.getPhysicalNumberOfRows();

            // TODO : 이후 로직 추가


        } catch (Exception e) {
            log.info("MissionParser.parseExcel() : {}", e.getMessage());
        }
    }
}

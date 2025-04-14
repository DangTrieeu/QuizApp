package com.caycon.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.caycon.model.Answer;
import com.caycon.model.Question;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public List<Question> readQuestions(String filePath) throws Exception {
        List<Question> questions = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue; // Bỏ qua header
            String content = row.getCell(0).getStringCellValue();
            double point = row.getCell(1).getNumericCellValue();
            List<Answer> answers = new ArrayList<>();
            for (int i = 2; i <= 5; i++) {
                String ansContent = row.getCell(i).getStringCellValue();
                boolean isCorrect = row.getCell(6).getStringCellValue().equals(String.valueOf(i - 1));
                answers.add(new Answer(0, 0, ansContent, isCorrect));
            }
            questions.add(new Question(0, content, "", point, answers));
        }
        workbook.close();
        fis.close();
        return questions;
    }
}
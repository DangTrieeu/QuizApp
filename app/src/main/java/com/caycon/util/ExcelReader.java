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
        System.out.println("Reading Excel file: " + filePath);
        List<Question> questions = new ArrayList<>();
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Sheet found with " + (sheet.getLastRowNum() + 1) + " rows");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    System.out.println("Skipping header row");
                    continue; // Bỏ qua header
                }

                // Đọc nội dung câu hỏi (cột 1)
                Cell contentCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String content = getCellStringValue(contentCell);
                if (content.isEmpty()) {
                    System.out.println("Skipping row " + row.getRowNum() + ": Empty question content");
                    continue;
                }

                // Đọc đáp án
                List<Answer> answers = new ArrayList<>();
                for (int i = 1; i <= 3; i++) { // Cột 2, 3, 4
                    Cell ansCell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String ansContent = getCellStringValue(ansCell);
                    if (!ansContent.isEmpty()) {
                        boolean isCorrect = (i == 1); // Cột 2 là đáp án đúng
                        answers.add(new Answer(0, 0, ansContent, isCorrect));
                    }
                }

                // Kiểm tra đáp án
                if (answers.size() < 3) {
                    System.out
                            .println("Skipping row " + row.getRowNum() + ": Only " + answers.size() + " answers found");
                    continue;
                }

                // Tạo câu hỏi
                Question question = new Question(0, content, "", 10.0, answers);
                questions.add(question);
                System.out.println("Added question: " + content + " with " + answers.size() + " answers");
            }

            System.out.println("Total questions read: " + questions.size());
            return questions;

        } catch (Exception e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            throw e;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    System.err.println("Error closing workbook: " + e.getMessage());
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
package com.project.service.business;

import com.project.payload.response.business.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);

    public ByteArrayInputStream generateExcel(List<OrderResponse> orders) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Sipariş Durumu","Müşteri Adı","Sipariş No", "Gasan No", "Sipariş Tarihi", "Teslim Tarihi", "Sipariş Türü", "Sipariş Adedi", "Hazır Mil Adedi"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyle);
            }

            // Log information about the number of orders
            LOGGER.info("Generating Excel for {} orders", orders.size());

            // Bılgılerı yazdır
            int rowNum = 1;
            for (OrderResponse order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getCustomerName());
                row.createCell(1).setCellValue(order.getOrderNumber());
                row.createCell(2).setCellValue(order.getGasanNo());
                row.createCell(3).setCellValue(order.getOrderDate());
                row.createCell(4).setCellValue(order.getDeliveryDate());
                row.createCell(5).setCellValue(order.getOrderType());
                row.createCell(6).setCellValue(order.getOrderQuantity());
                row.createCell(7).setCellValue(order.getReadyMilCount());
            }

            // Satır genişliklerini otomatik ayarla
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("orders_test.xlsx")) {
                workbook.write(fileOut);
                LOGGER.info("Excel file saved locally as orders_test.xlsx for debugging.");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        }catch (IOException e){
            LOGGER.error("Error occurred while generating Excel file: {}", e.getMessage());
            throw new RuntimeException("HExcel oluşturulurken hata oluştu: " + e);
        }
    }
}

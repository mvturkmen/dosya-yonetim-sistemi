package com.mehmetvasfi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    public void logOperation(String startDate, String endDate, String operationType,
            String operationCode, String statusCode,
            String sourcePath, double dataAmount) {
        try {
            MDC.put("start_date", startDate);
            MDC.put("end_date", endDate);
            MDC.put("operation_type", operationType);
            MDC.put("operation_code", operationCode);
            MDC.put("status_code", statusCode);
            MDC.put("source_path", sourcePath);
            MDC.put("data_amount", String.valueOf(dataAmount));

            logger.info("İşlem tamamlandı.");
        } finally {
            MDC.clear();
        }
    }
}

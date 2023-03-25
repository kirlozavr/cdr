package org.example;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.getDistinctionEntity();
        reportGenerator.writeReportFiles();
        reportGenerator.closeDB();
    }
}
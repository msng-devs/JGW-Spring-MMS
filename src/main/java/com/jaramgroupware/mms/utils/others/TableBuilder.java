package com.jaramgroupware.mms.utils.others;

import java.util.List;

public class TableBuilder {
    public static String toPrettyTable(List<String> columns, List<String> data) {
        // 각 열의 최대 너비를 계산
        int[] columnWidths = new int[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnWidths[i] = columns.get(i).length();
        }

        for (String row : data) {
            String[] rowData = row.split(",");
            for (int i = 0; i < rowData.length; i++) {
                if (rowData[i].length() > columnWidths[i]) {
                    columnWidths[i] = rowData[i].length();
                }
            }
        }

        // Pretty table 생성
        StringBuilder table = new StringBuilder();
        // 헤더
        for (int i = 0; i < columns.size(); i++) {
            table.append(String.format("%-" + columnWidths[i] + "s", columns.get(i))).append(" | ");
        }
        table.append("\n");

        // 구분선
        for (int i = 0; i < columns.size(); i++) {
            table.append("-".repeat(Math.max(0, columnWidths[i] + 3)));
        }
        table.append("\n");

        // 데이터 행
        for (String row : data) {
            String[] rowData = row.split(",");
            for (int i = 0; i < rowData.length; i++) {
                table.append(String.format("%-" + columnWidths[i] + "s", rowData[i])).append(" | ");
            }
            table.append("\n");
        }

        return table.toString();
    }
}

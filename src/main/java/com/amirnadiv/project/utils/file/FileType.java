package com.amirnadiv.project.utils.common.file;

import com.amirnadiv.project.utils.common.able.Valuable;
import com.amirnadiv.project.utils.common.file.csv.CsvProcessor;

public enum FileType implements Valuable<String> {
    CSV {
        public FileProcessor createProcessor() {
            return new CsvProcessor();
        }
    },
    XML, PDF, TXT, EXCEL, WORD;

    @Override
    public String value() {
        return this.name().toLowerCase();
    }

    public FileProcessor createProcessor() {
        return null;
    }

}

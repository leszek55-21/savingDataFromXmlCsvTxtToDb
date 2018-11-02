package com.leszekszymaszek.utils.fileNameFilters;

import com.leszekszymaszek.utils.TagNamesAndMessages;

import java.io.File;
import java.io.FilenameFilter;

public class CsvFileNameFilter implements FilenameFilter {

    // == PUBLIC METHODS ==
    @Override
    public boolean accept (File dir, String name) {

        return name.endsWith(TagNamesAndMessages.CSV_FILE_TYPE);
    }
}

package com.leszekszymaszek.utils.fileNameFilters;

import com.leszekszymaszek.utils.TagNamesAndMessages;

import java.io.File;
import java.io.FilenameFilter;

public class TxtFileNameFilter implements FilenameFilter {

    // == PUBLIC METHODS ==
    @Override
    public boolean accept (File dir, String name) {
        return name.endsWith(TagNamesAndMessages.TXT_FILE_TYPE);
    }
}

package com.leszekszymaszek.utils.fileNameFilters;

import com.leszekszymaszek.utils.TagNamesAndMessages;

import java.io.File;
import java.io.FilenameFilter;

public class XmlFileNameFilter implements FilenameFilter {

    // == PUBLIC METHODS ==
    @Override
    public boolean accept (File dir, String name) {

        return name.endsWith(TagNamesAndMessages.XML_FILE_TYPE);
    }
}

package com.personal.common.utilities;

import java.io.File;

/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class FileUtilities {
    /*
    * @param absoluteFileName File name with path
    * @return FileName minus the path   c:\myDir\MyFile.java"  -->MyFile.java
    */

    public static String extractFileName(String absoluteFileName) {

        /*if (!isNullOrEmpty(absoluteFileName)) {
            absoluteFileName = absoluteFileName.substring(absoluteFileName
                    .lastIndexOf(File.separatorChar) + 1, absoluteFileName.lastIndexOf("."));

        }*/
        return absoluteFileName;
    }
}

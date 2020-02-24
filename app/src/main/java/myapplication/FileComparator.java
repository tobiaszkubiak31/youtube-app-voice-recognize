package myapplication;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

    public int compare(File f1, File f2)
    {
        return (f1.getName().toLowerCase()).compareTo(f2.getName().toLowerCase());
    }

}

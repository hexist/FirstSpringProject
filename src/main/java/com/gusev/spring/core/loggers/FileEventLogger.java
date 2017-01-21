package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


/**
 * Created by Alexander on 30.12.2016.
 */
public class FileEventLogger implements EventLogger {
    private File file;
    private String filename;

    FileEventLogger(String filename){
        this.filename = filename;
    }

    public void init() throws IOException{
        this.file = new File(filename);
        if(file.exists() && !file.canWrite()){
            throw new IllegalArgumentException("Can't write to file" + filename);
        }else if(!file.exists()){
            file.createNewFile();
        }
    }

    public void logEvent(Event event) {
        try {
            FileUtils.writeStringToFile(file, event.toString() + '\n', Charset.defaultCharset(), true);
        } catch (IOException e) {
            System.out.println("Something wrong with saving too file");
            e.printStackTrace();
        }
    }
}

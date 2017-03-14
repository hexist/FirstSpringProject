package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class FileEventLogger extends AbstractEventLogger {

    private File file;

    @Value("${log.file:target/log.txt}")
    private String filename;

    public FileEventLogger(){
    }

    public FileEventLogger(String filename){
        this.filename = filename;
    }

    @PostConstruct
    public void init() throws IOException{
        this.file = new File(filename);
        if(file.exists() && !file.canWrite()){
            throw new IllegalArgumentException("Can't write to file" + filename);
        }else if(!file.exists()){
            file.createNewFile();
        }
    }

    @Override
    public void logEvent(Event event) {
        try {
            FileUtils.writeStringToFile(file, event.toString() + '\n', Charset.defaultCharset(), true);
        } catch (IOException e) {
            System.out.println("Something wrong with saving too file");
            e.printStackTrace();
        }
    }

    @Value("File Event Logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}

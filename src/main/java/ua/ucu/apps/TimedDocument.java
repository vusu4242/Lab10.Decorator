package ua.ucu.apps;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimedDocument implements Document {
    private Document document;
    
    @Override
    public String parse() {
        long startTime = System.currentTimeMillis();
        String parsed = document.parse();
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + " ms");
        return parsed;
    }
    
    @Override
    public String getGcsPath() {
        return document.getGcsPath();
    }
}
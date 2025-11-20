package ua.ucu.apps;

import lombok.Getter;
import lombok.Setter;

public class MockedDocument implements Document {
    @Getter
    @Setter
    public String gcsPath;
    
    @Getter
    @Setter
    private String content;
    
    public MockedDocument() {
        this.content = "Mocked content";
    }
    
    @Override
    public String parse() {
        return content;
    }
}
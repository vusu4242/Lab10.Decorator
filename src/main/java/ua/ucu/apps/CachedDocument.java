package ua.ucu.apps;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CachedDocument implements Document {
    private Document document;
    
    @Override
    public String parse() {
        // 1. Verify if path exists in DB
        String parsedFromDB = DBConnection.getInstance().getParsedByPath(getGcsPath());
        
        // 2. If exists - return parsed from db
        if (parsedFromDB != null) {
            return parsedFromDB;
        } else {
            // 3. document.parse();
            String parsed = document.parse();
            
            // 4. Save parsed into DB
            DBConnection.getInstance().createDocument(getGcsPath(), parsed);
            
            // 5. Return parsed
            return parsed;
        }
    }
    
    @Override
    public String getGcsPath() {
        return document.getGcsPath();
    }
}
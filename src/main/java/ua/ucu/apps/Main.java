package ua.ucu.apps;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        
        MockedDocument document = new MockedDocument();
        document.setGcsPath("/Test/path/2");
        document.setContent("Test content");
        
        CachedDocument cacheDocument = new CachedDocument(document);
        
        System.out.println("First call:");
        System.out.println(cacheDocument.parse());
        
        System.out.println("\nSecond call (cached):");
        System.out.println(cacheDocument.parse());
    }
}
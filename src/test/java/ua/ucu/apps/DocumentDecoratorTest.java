package ua.ucu.apps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentDecoratorTest {
    private MockedDocument mockDocument;
    
    @BeforeEach
    void setUp() {
        mockDocument = new MockedDocument();
        mockDocument.setGcsPath("/test/path.txt");
        mockDocument.setContent("Test content");
    }
    
    @Test
    void testBasicMockedDocument() {
        String result = mockDocument.parse();
        assertEquals("Test content", result);
        assertEquals("/test/path.txt", mockDocument.getGcsPath());
    }
    
    @Test
    void testTimedDocument() {
        Document timedDoc = new TimedDocument(mockDocument);
        String result = timedDoc.parse();
        assertEquals("Test content", result);
    }
    
    @Test
    void testCachedDocumentFirstCall() {
        Document cachedDoc = new CachedDocument(mockDocument);
        String result = cachedDoc.parse();
        assertEquals("Test content", result);
    }
    
    @Test
    void testCachedDocumentSecondCall() {
        Document cachedDoc = new CachedDocument(mockDocument);
        
        // First call
        String result1 = cachedDoc.parse();
        
        // Second call should return cached
        String result2 = cachedDoc.parse();
        
        assertEquals(result1, result2);
        assertEquals("Test content", result2);
    }
    
    @Test
    void testCombinedTimedAndCached() {
        Document combinedDoc = new TimedDocument(
            new CachedDocument(mockDocument)
        );
        
        String result = combinedDoc.parse();
        assertEquals("Test content", result);
    }
    
    @Test
    void testDecoratorPreservesGcsPath() {
        Document timedDoc = new TimedDocument(mockDocument);
        Document cachedDoc = new CachedDocument(mockDocument);
        
        assertEquals("/test/path.txt", timedDoc.getGcsPath());
        assertEquals("/test/path.txt", cachedDoc.getGcsPath());
    }
    
    @Test
    void testMultipleDecorators() {
        Document doc = new TimedDocument(
            new CachedDocument(
                new TimedDocument(mockDocument)
            )
        );
        
        String result = doc.parse();
        assertEquals("Test content", result);
        assertEquals("/test/path.txt", doc.getGcsPath());
    }
}
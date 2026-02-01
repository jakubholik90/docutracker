package pl.jakubholik90.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {

    @Test
    public void testCreateNewDocument() {

        Document document = new Document("XXX.pdf", 1234);

        //checking values for new created values
        assertEquals("XXX.pdf",document.getFileName()); //checking assign fileName
        assertEquals(1234,document.getProjectId().intValue()); //checking assing projectId
        System.out.println(document.getDocumentId());
        assertTrue(document.getLastStatusChange().isBefore(LocalDateTime.now())); //checking assign lastStatusChange
        assertTrue(document.getDocumentId()>=0); //checking assign documentId
        System.out.println(document.getLastStatusChange());
        assertEquals(DocumentStatus.NONE,document.getStatus()); // checking assign documentStatus
    }

    @Test
    public void checkingIdCounter() {
        Document document0 = new Document("test0.pdf", 1);
        Document document1 = new Document("test1.pdf", 1);
        assertTrue(document1.getDocumentId()>document0.getDocumentId());
        System.out.println(document0.getDocumentId());
        System.out.println(document1.getDocumentId());
    }
}

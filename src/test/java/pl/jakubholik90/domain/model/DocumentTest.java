package pl.jakubholik90.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {

    @Test
    public void testCreateNewDocument() {

        Document document = Document.builder()
                .fileName("XXX.pdf")
                .documentId(1234)
                        .build();

        //checking values for new created values
        assertEquals("XXX.pdf",document.getFileName()); //checking assign fileName
        assertEquals(1234,document.getProjectId().intValue()); //checking assing projectId
        System.out.println(document.getDocumentId());
        assertTrue(document.getLastStatusChange().isBefore(LocalDateTime.now())); //checking assign lastStatusChange
        assertTrue(document.getDocumentId()>=0); //checking assign documentId
        System.out.println(document.getLastStatusChange());
        assertEquals(DocumentStatus.DRAFT,document.getStatus()); // checking assign documentStatus
    }

    @Test
    public void checkingIdCounter() {
        Document document0 = Document.builder()
                .fileName("test0.pdf")
                .documentId(1)
                .build();
        Document document1 = Document.builder()
                .fileName("test1.pdf")
                .documentId(2)
                .build();
        assertTrue(document1.getDocumentId()>document0.getDocumentId());
        System.out.println(document0.getDocumentId());
        System.out.println(document1.getDocumentId());
    }
}

package ma.emsi.linahannouni.emsibot.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentIndexor {

    private final VectorStore vectorStore;

    public DocumentIndexor(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Indexe un fichier PDF dans le VectorStore
     * 
     * @param pdfFile le fichier PDF à indexer
     */
    public void ingestPdf(File pdfFile) {
        Resource resource = new FileSystemResource(pdfFile);
        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(resource);

        List<Document> documents = pdfDocumentReader.get();
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> chunks = textSplitter.apply(documents);

        // Ajouter les chunks au vector store
        vectorStore.add(chunks);

        // Sauvegarder le vector store sur disque
        if (vectorStore instanceof SimpleVectorStore simpleVectorStore) {
            File storeFile = new File(VectorStoreConfig.STORE_FILE_PATH);
            simpleVectorStore.save(storeFile);
        }
    }

    /**
     * Recherche des documents similaires à la requête
     * 
     * @param query la question de l'utilisateur
     * @param topK  nombre de résultats à retourner
     * @return liste de documents pertinents
     */
    public List<Document> searchDocuments(String query, int topK) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }

    /**
     * Recherche et retourne le contexte sous forme de texte
     * 
     * @param query la question de l'utilisateur
     * @return le contexte pertinent en texte
     */
    public String getRelevantContext(String query) {
        List<Document> documents = searchDocuments(query, 5);
        if (documents.isEmpty()) {
            return "";
        }
        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}

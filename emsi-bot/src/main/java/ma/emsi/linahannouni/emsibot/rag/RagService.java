package ma.emsi.linahannouni.emsibot.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service principal pour la gestion du RAG (Retrieval-Augmented Generation)
 * Fournit des opérations de haut niveau pour l'indexation et la recherche
 */
@Service
public class RagService {

    private final DocumentIndexor documentIndexor;
    private final VectorStore vectorStore;

    public RagService(DocumentIndexor documentIndexor, VectorStore vectorStore) {
        this.documentIndexor = documentIndexor;
        this.vectorStore = vectorStore;
    }

    /**
     * Indexe un fichier PDF
     * 
     * @param pdfFile le fichier PDF à indexer
     */
    public void indexPdf(File pdfFile) {
        documentIndexor.ingestPdf(pdfFile);
    }

    /**
     * Recherche les documents les plus pertinents pour une requête
     * 
     * @param query la requête de recherche
     * @param topK  nombre de résultats à retourner
     * @return liste des documents pertinents
     */
    public List<Document> search(String query, int topK) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }

    /**
     * Recherche et retourne le contexte sous forme de texte formaté
     * 
     * @param query la requête de recherche
     * @return le contexte pertinent en texte
     */
    public String getContext(String query) {
        return getContext(query, 5);
    }

    /**
     * Recherche et retourne le contexte sous forme de texte formaté
     * 
     * @param query la requête de recherche
     * @param topK  nombre maximum de documents à inclure
     * @return le contexte pertinent en texte
     */
    public String getContext(String query, int topK) {
        List<Document> documents = search(query, topK);
        if (documents.isEmpty()) {
            return "";
        }
        return documents.stream()
                .map(doc -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(doc.getText());
                    // Ajouter les métadonnées si disponibles
                    if (doc.getMetadata() != null && !doc.getMetadata().isEmpty()) {
                        sb.append("\n[Source: ").append(doc.getMetadata()).append("]");
                    }
                    return sb.toString();
                })
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    /**
     * Vérifie si des documents sont indexés
     * 
     * @return true si le vector store contient des documents
     */
    public boolean hasIndexedDocuments() {
        try {
            List<Document> testSearch = search("test", 1);
            return testSearch != null && !testSearch.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}

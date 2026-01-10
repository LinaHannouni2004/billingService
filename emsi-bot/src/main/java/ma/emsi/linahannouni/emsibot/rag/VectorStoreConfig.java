package ma.emsi.linahannouni.emsibot.rag;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class VectorStoreConfig {

    public static final String STORE_FILE_PATH = "vector-store/store.json";

    @Bean
    public VectorStore getVectorStore(EmbeddingModel model) {
        // Créer le dossier vector-store s'il n'existe pas
        File storeDir = new File("vector-store");
        if (!storeDir.exists()) {
            storeDir.mkdirs();
        }

        File storeFile = new File(STORE_FILE_PATH);

        SimpleVectorStore vectorStore = SimpleVectorStore.builder(model)
                .build();

        // Charger le store existant s'il existe
        if (storeFile.exists()) {
            vectorStore.load(storeFile);
        }

        return vectorStore;
    }
}

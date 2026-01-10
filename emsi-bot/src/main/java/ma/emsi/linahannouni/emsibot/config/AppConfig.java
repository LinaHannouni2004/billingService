package ma.emsi.linahannouni.emsibot.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration globale de l'application
 * Contient les beans nécessaires pour le fonctionnement du bot
 */
@Configuration
public class AppConfig {

    /**
     * Bean pour la mémoire de conversation
     * Utilise MessageWindowChatMemory pour stocker l'historique des conversations
     * avec une fenêtre de 10 messages
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
    }
}

package ma.emsi.linahannouni.emsibot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AIAgent {

        private ChatClient chatClient;

        public AIAgent(ChatClient.Builder builder, ChatMemory chatMemory, VectorStore vectorStore,
                        @Autowired(required = false) ToolCallbackProvider tools) {

                // Afficher les outils disponibles si présents
                if (tools != null) {
                        Arrays.stream(tools.getToolCallbacks())
                                        .forEach(toolCallBack -> {
                                                System.out.println("----------------");
                                                System.out.println(toolCallBack.getToolDefinition());
                                                System.out.println("----------------");
                                        });
                }

                // Créer le RAG Advisor avec QuestionAnswerAdvisor de Spring AI
                QuestionAnswerAdvisor ragAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                                .build();

                // Construire le client de chat
                ChatClient.Builder clientBuilder = builder
                                .defaultSystem("""
                                                 Vous êtes un assistant intelligent qui se charge
                                                 de répondre aux questions de l'utilisateur en fonction
                                                 du contexte fourni par les documents indexés.

                                                 Si un contexte RAG est fourni, basez votre réponse sur ce contexte.
                                                 Si aucun contexte pertinent n'est trouvé, vous pouvez répondre
                                                 en utilisant vos connaissances générales, mais précisez-le.

                                                 Soyez précis, utile et courtois dans vos réponses.
                                                """)
                                .defaultAdvisors(
                                                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                                                ragAdvisor);

                // Ajouter les outils seulement s'ils sont disponibles
                if (tools != null) {
                        clientBuilder.defaultToolCallbacks(tools);
                }

                this.chatClient = clientBuilder.build();
        }

        public String askAgent(Prompt message) {
                return chatClient.prompt(message)
                                .call().content();
        }
}

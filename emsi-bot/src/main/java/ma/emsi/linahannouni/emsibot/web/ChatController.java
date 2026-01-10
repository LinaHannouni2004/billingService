package ma.emsi.linahannouni.emsibot.web;


import ma.emsi.linahannouni.emsibot.agents.AIAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

private AIAgent agent;

public ChatController(AIAgent agent) {
        this.agent = agent;
}

@GetMapping(value = "/chat",produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(String message) {
return agent.askAgent(new Prompt(message));

    }

}

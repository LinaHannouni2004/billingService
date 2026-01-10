package ma.emsi.linahannouni.emsibot.telegram;


import io.micrometer.core.instrument.docs.MeterDocumentation;
import jakarta.annotation.PostConstruct;
import ma.emsi.linahannouni.emsibot.agents.AIAgent;
import ma.emsi.linahannouni.emsibot.rag.DocumentIndexor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

@Autowired
    DocumentIndexor documentIndexor;

    @Value("${telegram.api.key}")
    private String telegramBotToken;

    private AIAgent aiAgent;

    public TelegramBot(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update telegraRequest) {
        try {
            if(!telegraRequest.hasMessage()) return;
            String messageText = telegraRequest.getMessage().getText();
            Long chatId = telegraRequest.getMessage().getChatId();

            //gestion rag
            if(telegraRequest.getMessage().hasDocument()){
                Document doc = telegraRequest.getMessage().getDocument();
                if(doc.getMimeType().equals("application/pdf")){
                    File file = execute(new GetFile(doc.getFileId()));
                    java.io.File pdf= downloadFile(file,new java.io.File("uploads/"+doc.getFileName()));

                    //indexer le pdf
                    documentIndexor.ingestPdf(pdf);

                    sendTextMessage(chatId,
                            "pdf recu et indexe tu peux poser des questions."
                            );
                    return;
                }



            }

            //gestion rag

            List<PhotoSize> photos = telegraRequest.getMessage().getPhoto();
            List<Media> mediaList = new ArrayList<>();
            String caption =null;
            if (photos!=null){
                caption = telegraRequest.getMessage().getCaption();
                if (caption==null) caption="What do see in this image";

                for (PhotoSize ps : photos){
                    String fileId = ps.getFileId();
                    GetFile getFile = new GetFile();
                    getFile.setFileId(fileId);
                    File file = execute(getFile);
                    String filePath = file.getFilePath();
                    String textUrl = "https://api.telegram.org/file/bot"
                            +getBotToken()+"/"+filePath;
                    URL fileUrl = new URL(textUrl);
                    mediaList.add(Media.builder()
                            .id(fileId)
                            .mimeType(MimeTypeUtils.IMAGE_PNG)
                            .data(new UrlResource(fileUrl))
                            .build());
                }
            }




            String query = messageText!=null?messageText:caption;
            UserMessage userMessage = UserMessage.builder()
                    .text(query)
                    .media(mediaList)
                    .build();
            sendTypingquestion(chatId);
            String answer = aiAgent.askAgent(new Prompt(userMessage));
            sendTextMessage(chatId, answer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "EmsilinaAiBot";
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),text);
        execute(sendMessage);
    }
private void sendTypingquestion(long chatId) throws TelegramApiException {
    SendChatAction sendChatAction = new SendChatAction();
    sendChatAction.setChatId(String.valueOf(chatId));
    sendChatAction.setAction(ActionType.TYPING);
    execute(sendChatAction);
}
}

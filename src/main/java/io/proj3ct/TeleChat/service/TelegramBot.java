package io.proj3ct.TeleChat.service;

import io.proj3ct.TeleChat.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }
    /**
     * Обработка запроса пользователя и возвращение ответа
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) { //убедились что нам что то прислали и там есть текст
            String messageText = update.getMessage().getText(); //получили текст
            long chatID = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatID, update.getMessage().getChat().getFirstName());
                    break;
                default:
                        sendMessage(chatID,"Sorry, command was not recognized");
            }
        }
    }

    /**
     * Команда запуска
     */
    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        try {
            sendMessage(chatId,answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Отправка сообщений
     * @param textToSend текст для отправки
     */
    private void sendMessage(long chatId, String textToSend) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //long в String особенности телеграмм
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
}

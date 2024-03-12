package weteam.backend.application.slack;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
public class SlackService {
  private final Slack slackClient = Slack.getInstance();
  @Value("${webhook.slack.url}")
  private String SLACK_WEBHOOK_URL;

  public void sendMessage(String title, HashMap<String, String> data) {
    try {
      slackClient.send(SLACK_WEBHOOK_URL, payload(p -> p
          .text(title) // 메시지 제목
          .attachments(List.of(
              Attachment.builder().color(Color.GREEN.toString()) // 메시지 색상
                  .fields( // 메시지 본문 내용
                      data.keySet().stream().map(key -> generateSlackField(key, data.get(key))).collect(Collectors.toList())
                  ).build())))
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Slack Field 생성
   **/
  private Field generateSlackField(String title, String value) {
    return Field.builder()
        .title(title)
        .value(value)
        .valueShortEnough(false)
        .build();
  }
}

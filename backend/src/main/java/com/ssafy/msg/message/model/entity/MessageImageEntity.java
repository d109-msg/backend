package com.ssafy.msg.message.model.entity;


import com.ssafy.msg.message.model.dto.MessageImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "message_images")
public class MessageImageEntity {

    @Id
    private String id;
    private String url;
    private String uuid;

    public MessageImageDto toDto() {
        return MessageImageDto.builder()
                .url(this.url)
                .uuid(this.uuid)
                .build();
    }
}

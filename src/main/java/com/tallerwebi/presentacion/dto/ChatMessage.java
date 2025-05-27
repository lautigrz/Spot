package com.tallerwebi.presentacion.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

 private String sender;
 private String content;
 private String image;
 private MessageType type;

 public enum MessageType {
   CHAT, LEAVE, JOIN
 }
}

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
 private String id;

 public enum MessageType {
   CHAT, LEAVE, JOIN
 }
}

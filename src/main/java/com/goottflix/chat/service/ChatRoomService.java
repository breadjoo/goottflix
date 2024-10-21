package com.goottflix.chat.service;

import com.goottflix.chat.entity.ChatRoom;
import com.goottflix.chat.entity.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {


    private final ChatRoomMapper chatRoomMapper;


    public List<ChatRoom> getAllChatRooms() {
        return chatRoomMapper.getAllChatRooms();
    }

    public ChatRoom getChatRoomById(Long id) {
        return chatRoomMapper.getChatRoomById(id);
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        chatRoomMapper.createChatRoom(chatRoom);
        return chatRoom;
    }

    public int deleteChatRoom(Long roomId) {
        return chatRoomMapper.deleteChatRoom(roomId);
    }

    public String getChatRoomNameById(Long id) {
        return chatRoomMapper.getChatRoomNameById(id);
    }

}

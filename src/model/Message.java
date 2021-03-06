package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author X550V
 */
public class Message implements java.io.Serializable {

    public static enum Message_Type {
        JoinServer, CreateRoom, ReturnRoomsNames, 
        JoinRoom, MovePiece, Attack, 
        Text, Selected, CheckMate,
        Start, Upgrade, Ready, 
        ExitRoom, CloseRoom,
        Login, Register, LoginFailed, 
        LoginSucced, EnemyLoss, Chat, 
        Sigup, RefreshHis, GetFriendReq, 
        SendFriendReq, AcceptReq, GetFriendList,
        SearchUser, ReloadFriendReq, Disconnect,
        OpenMic, CloseMic, OpenSpeaker, CloseSpeaker,
        GetAudioPort

    }

    public Message(Message_Type type, Object content) {
        this.type = type;
        this.content = content;
    }

    public Message_Type type;
    public Object content;

    public Message(Message_Type t) {
        this.type = t;
    }

}

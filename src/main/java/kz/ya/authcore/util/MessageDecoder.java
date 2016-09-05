/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.util;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import kz.ya.authcore.dto.InData;
import kz.ya.authcore.dto.Message;

/**
 *
 * @author YERLAN
 */
public class MessageDecoder implements Decoder.Text<Message> {
    
    @Override
    public Message decode(String arg0) throws DecodeException {
        if (arg0.isEmpty()) {
            return null;
        }

        JsonObject jsonRequest = Json.createReader(new StringReader(arg0)).readObject();

        Message message = new Message(jsonRequest.getString("sequence_id"));
        message.setType(jsonRequest.getString("type"));

        JsonObject data = jsonRequest.getJsonObject("data");
        if (data != null) {
            InData inData = new InData();
            inData.setEmail(data.getString("email"));
            inData.setPassword(data.getString("password"));
            message.setData(inData);
        }

        return message;
    }

    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}

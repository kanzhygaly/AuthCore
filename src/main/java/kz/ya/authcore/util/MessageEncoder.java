/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.util;

import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import kz.ya.authcore.dto.ErrorData;
import kz.ya.authcore.dto.Message;
import kz.ya.authcore.dto.SuccessData;

/**
 *
 * @author YERLAN
 */
public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(Message arg0) throws EncodeException {
        if (arg0 == null) {
            return "";
        }
        System.out.println("Encoder: " + arg0);
        
        JsonObjectBuilder builder = Json.createObjectBuilder().add("type", arg0.getType()).add("sequence_id", arg0.getSequenceId());

        if ("CUSTOMER_API_TOKEN".equals(arg0.getType())) {
            SuccessData data = (SuccessData) arg0.getData();
            builder.add("data", Json.createObjectBuilder()
                    .add("api_token", data.getApiToken())
                    .add("api_token_expiration_date", data.getApiTokenExpirationDate()).build()
            );
        } else if ("CUSTOMER_ERROR".equals(arg0.getType())) {
            ErrorData data = (ErrorData) arg0.getData();
            builder.add("data", Json.createObjectBuilder()
                    .add("error_description", data.getErrorDescription())
                    .add("error_code", data.getErrorCode()).build()
            );
        }

        JsonObject jsonResponse = builder.build();

        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = Json.createWriter(stringWriter)) {
            writer.writeObject(jsonResponse);
        }
        return stringWriter.getBuffer().toString();
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}

package com.ebizcipta.ajo.api.exception;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class NewException {
    public void convertExceptionMessage(String ex, String entity, String errorKey){
        try {
            JSONObject jsonObject = new JSONObject(ex);
            String message_error = (String) jsonObject.get("message");
            throw new BadRequestAlertException(message_error, entity, errorKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


package com.fantasysport.parsers.jackson;

import com.fantasysport.models.RosterType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by bylynka on 6/24/14.
 */
public class RosterTypeDeserializer extends JsonDeserializer<RosterType> {
    @Override
    public RosterType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateStr = jsonParser.getText();
        if(dateStr == null || dateStr.length() == 0){
            return RosterType.Default;
        }
        try{
            return RosterType.valueOf(dateStr);
        }catch (Exception ex){
            return RosterType.Default;
        }
    }
}

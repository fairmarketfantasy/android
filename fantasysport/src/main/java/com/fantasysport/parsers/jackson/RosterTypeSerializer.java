package com.fantasysport.parsers.jackson;

import com.fantasysport.models.RosterType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by bylynka on 6/24/14.
 */
public class RosterTypeSerializer extends JsonSerializer<RosterType> {
    @Override
    public void serialize(RosterType rosterType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(rosterType.toString());
    }
}

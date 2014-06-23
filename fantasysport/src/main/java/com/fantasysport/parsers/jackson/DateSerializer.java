package com.fantasysport.parsers.jackson;

import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * Created by bylynka on 6/20/14.
 */
public class DateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        int gmtInMinutes = DeviceInfo.getGMTInMinutes();
        value = DateUtils.addMinutes(value, -1 * gmtInMinutes);
        String dateStr = Converter.toString(value);
        jgen.writeString(dateStr);
    }
}

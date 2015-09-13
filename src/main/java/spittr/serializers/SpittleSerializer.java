package spittr.serializers;

import java.io.IOException;

import spittr.entity.Spittle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SpittleSerializer extends JsonSerializer<Spittle> {
    @Override
    public void serialize(Spittle value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("message", value.getMessage());
       
        jgen.writeObjectFieldStart("spitter");
        jgen.writeObjectField("id", value.getSpitter().getId());
        jgen.writeEndObject();
         
        jgen.writeEndObject();
    }
}
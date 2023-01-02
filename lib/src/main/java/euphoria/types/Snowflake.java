package euphoria.types;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Snowflake {
    public static final int length = 13;
    private static final String hexRegex = "^[\\dA-Za-z]+$";
    char[] chars;

    public Snowflake(String snowflakeText){
        if(snowflakeText==null){
            throw new NullPointerException("Snowflake text was null");
        }
        if(snowflakeText.length()!=length){
            throw new IllegalArgumentException("Snowflake text "+snowflakeText+" doesn't match length "+length);
        }
        if(!snowflakeText.matches(hexRegex)){
            throw new IllegalArgumentException("Snowflake text "+snowflakeText+" doesn't match regex "+hexRegex);
        }
        chars=snowflakeText.toLowerCase().toCharArray();
    }

    @Override
    public String toString() {
        return new String(chars);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chars);
    }

    //todo implement snowflake making from time like euphoria


    public static Snowflake random() {
        String thing = Long.toUnsignedString(ThreadLocalRandom.current().nextLong(),36);
        return new Snowflake("0".repeat(length-thing.length())+thing);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Snowflake other = (Snowflake) obj;
        return Arrays.equals(chars,other.chars);
    }

    public static class SnowflakeAdapter implements JsonSerializer<Snowflake>, JsonDeserializer<Snowflake> {

        @Override
        public Snowflake deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Snowflake(json.getAsString());
        }

        @Override
        public JsonElement serialize(Snowflake src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

}

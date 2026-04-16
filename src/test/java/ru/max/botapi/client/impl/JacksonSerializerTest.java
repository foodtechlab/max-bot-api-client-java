package ru.max.botapi.client.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import ru.max.botapi.UnitTest;
import ru.max.botapi.exceptions.SerializationException;
import ru.max.botapi.model.MessageBody;
import ru.max.botapi.model.SendMessageResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@Category(UnitTest.class)
public class JacksonSerializerTest {
    private JacksonSerializer serializer = new JacksonSerializer();

    @Test(expected = SerializationException.class)
    public void shouldThrowSerializationException() throws Exception {
        serializer.serialize(new NotSerializableClass());
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowSerializationException2() throws Exception {
        serializer.serializeToString(new NotSerializableClass());
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowSerializationExceptionOnDeserialization() throws Exception {
        serializer.deserialize("{", MessageBody.class);
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowSerializationExceptionOnDeserialization2() throws Exception {
        serializer.deserialize(new byte[]{1, 2, 3}, MessageBody.class);
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowSerializationExceptionOnDeserialization3() throws Exception {
        serializer.deserialize(new ByteArrayInputStream(new byte[]{1, 2, 3}), MessageBody.class);
    }

    @Test
    public void shouldReturnNullOnEmptyInput() throws Exception {
        assertThat(serializer.deserialize((byte[]) null, Object.class), is(nullValue()));
        assertThat(serializer.deserialize((String) null, Object.class), is(nullValue()));
        assertThat(serializer.deserialize((InputStream) null, Object.class), is(nullValue()));
        assertThat(serializer.deserialize("", Object.class), is(nullValue()));
    }

    @Test
    public void shouldReturnNullOnSerialize() throws Exception {
        assertThat(serializer.serialize(null), is(nullValue()));
        assertThat(serializer.serializeToString(null), is(nullValue()));
    }

    @Test
    public void testSerializeToString() throws Exception {
        MessageBody object = new MessageBody("mid", 1L, "text", Collections.emptyList());
        String serialized = serializer.serializeToString(object);
        MessageBody deserialized = serializer.deserialize(serialized, MessageBody.class);
        assertThat(deserialized, is(object));
    }

    @Test
    public void testSerialize() throws Exception {
        MessageBody object = new MessageBody("mid", 1L, "text", Collections.emptyList());
        byte[] serialized = serializer.serialize(object);
        MessageBody deserialized = serializer.deserialize(serialized, MessageBody.class);
        assertThat(deserialized, is(object));
    }

    @Test
    public void testDeserializeStream() throws Exception {
        MessageBody object = new MessageBody("mid", 1L, "text", Collections.emptyList());
        byte[] serialized = serializer.serialize(object);
        MessageBody deserialized = serializer.deserialize(new ByteArrayInputStream(serialized), MessageBody.class);
        assertThat(deserialized, is(object));
    }

    @Test
    public void shouldDeserializeSendMessageResponseWithNestedMessageBody() throws Exception {
        String payload = "{\"message\":{\"recipient\":{\"chat_id\":146034524,\"chat_type\":\"dialog\",\"user_id\":103604639},"
                + "\"timestamp\":1776275450264,"
                + "\"sender\":{\"user_id\":244887235,\"first_name\":\"Test bot\",\"username\":\"id3444197280_bot\",\"is_bot\":true,"
                + "\"last_activity_time\":1776275450276,\"name\":\"Test bot\"},"
                + "\"message\":{\"mid\":\"mid.0000000008b44f5c019d924469981adc\",\"seq\":116409987908508380,"
                + "\"text\":\"MAX raw test\"}},"
                + "\"chat_id\":146034524,"
                + "\"recipient_id\":146034524,"
                + "\"message_id\":\"mid.0000000008b44f5c019d924469981adc\"}";

        SendMessageResult deserialized = serializer.deserialize(payload, SendMessageResult.class);

        assertThat(deserialized, is(notNullValue()));
        assertThat(deserialized.getMessage(), is(notNullValue()));
        assertThat(deserialized.getMessage().getBody(), is(notNullValue()));
        assertThat(deserialized.getMessage().getBody().getMid(), is("mid.0000000008b44f5c019d924469981adc"));
    }

    private static class NotSerializableClass {
        private final NotSerializableClass self = this;

        @Override
        public String toString() {
            return self.getClass().getName();
        }
    }
}

package com.project.cookbook.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

public class InOutService {
    public static <T extends Message> T readMessageForType(String json, T type) throws InvalidProtocolBufferException {
        JsonFormat.Parser parser = JsonFormat.parser();
        Message.Builder builder = type.toBuilder();
        parser.merge(json, builder);
        return (T) builder.build();
    }

    public static <T extends Message> String write(T message) throws InvalidProtocolBufferException {
        JsonFormat.Printer printer = JsonFormat.printer();
        return printer.print(message);
    }
}

package ru.entryset.chat.message;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class Convector {

    public static HashMap<UUID, Long> deserializeMap(String item) {
        HashMap<UUID, Long> stack = null;
        try {
            byte[] serializedObject = Base64.getDecoder().decode(item);
            ByteArrayInputStream io = new ByteArrayInputStream(serializedObject);
            ObjectInputStream os = new ObjectInputStream(io);
            stack = (HashMap<UUID, Long>) os.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        if(stack == null){
            return new HashMap<>();
        }

        return stack;
    }

    public static String serializeMap(HashMap<UUID, Long> item) {

        String encodedObject = null;

        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            byte[] serializedObject = io.toByteArray();
            encodedObject = Base64.getEncoder().encodeToString(serializedObject);
        } catch (IOException e){
            e.printStackTrace();
        }
        return encodedObject;
    }
}

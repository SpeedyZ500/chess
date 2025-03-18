package gson;

import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

public class ChessBoardMapSerializer implements JsonSerializer<Map<ChessPosition, ChessPiece>> {
    @Override
    public JsonElement serialize(Map<ChessPosition, ChessPiece> src, Type type, JsonSerializationContext context) {
        JsonObject boardJson = new JsonObject();
        for (Map.Entry<ChessPosition, ChessPiece> entry : src.entrySet()){
            String key = entry.getKey().getRow() + "," + entry.getKey().getColumn();
            boardJson.add(key, context.serialize(entry.getValue()));
        }
        return boardJson;
    }
}

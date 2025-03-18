package gson;

import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardMapDeserializer implements JsonDeserializer<Map<ChessPosition, ChessPiece>> {
    @Override
    public Map<ChessPosition, ChessPiece> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject boardJson = json.getAsJsonObject();
        Map<ChessPosition, ChessPiece> board = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : boardJson.entrySet()){
            String [] key = entry.getKey().split(",");
            int row = Integer.parseInt(key[0]);
            int col = Integer.parseInt(key[1]);
            ChessPiece piece = context.deserialize(entry.getValue(), ChessPiece.class);
            board.put(new ChessPosition(row, col), piece);
        }
        return board;
    }
}

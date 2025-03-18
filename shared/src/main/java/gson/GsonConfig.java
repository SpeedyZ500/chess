package gson;

import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class GsonConfig {
    public static final Gson createGson(){
        return new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<Map<ChessPosition, ChessPiece>>() {}.getType(),
                        new ChessBoardMapSerializer()
                )
                .registerTypeAdapter(
                        new TypeToken<Map<ChessPosition, ChessPiece>>() {}.getType(),
                        new ChessBoardMapDeserializer()
                )
                .create();
    }
}

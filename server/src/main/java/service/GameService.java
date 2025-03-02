package service;

import dataaccess.gamedao.GameDAO;

public class GameService {
    private final GameDAO gameDAO;
    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
}

package model;

import chess.ChessGame;

public record GameDataNoGame(
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName){}


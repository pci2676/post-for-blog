package com.javabom.springdatajdbc.chess.game;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("CHESSGAME")
public class ChessGame {
    @Id
    private Long id;
    private String name;
    private boolean active;

    protected ChessGame() {
    }

    public ChessGame(final String name, final boolean active) {
        this.name = name;
        this.active = active;
    }
}

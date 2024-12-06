package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;
    private final String authToken;
    private final ChessGame.TeamColor teamColor;
    private final Integer gameID;
    private final ChessMove move;


    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public UserGameCommand(CommandType commandType, String authToken, ChessGame.TeamColor teamColor, Integer gameID, ChessMove chessMove) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.teamColor = teamColor;
        this.gameID = gameID;
        this.move = chessMove;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        GET_GAME
    }
    public ChessMove getMove() {
        return move;
    }
    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}

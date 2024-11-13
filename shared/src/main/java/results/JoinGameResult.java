package results;

import model.GameData;

public record JoinGameResult(GameData gameData, boolean colorAvailable) {
}

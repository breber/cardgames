package com.worthwhilegames.cardgames;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsGame;
import com.worthwhilegames.cardgames.crazyeights.GameboardFragment;
import com.worthwhilegames.cardgames.crazyeights.PlayerHandFragment;
import com.worthwhilegames.cardgames.player.activities.GameResultsActivity;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.game.BaseGameActivity;

import java.util.ArrayList;

/**
 * Created by breber on 2/2/14.
 */
public class GameActivity extends BaseGameActivity implements
        OnTurnBasedMatchUpdateReceivedListener, PlayerHandFragment.GameUpdatedListener {

    private static final String TAG = GameActivity.class.getSimpleName();

    private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());
    private static final int SELECT_OPPONENTS = Math.abs("SELECT_OPPONENTS".hashCode());
    private static final int LOOK_AT_MATCHES = Math.abs("LOOK_AT_MATCHES".hashCode());

    public static String CREATE_GAME_EXTRA = "CREATEGAME";

    private GameboardFragment mGameboardFragment;
    private PlayerHandFragment mPlayerHandFragment;
    private TurnBasedMatch mMatch;
    private boolean mCreateGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        mCreateGame = i.getBooleanExtra(CREATE_GAME_EXTRA, false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        if (Util.isDebugBuild()) {
            enableDebugLog(true);
        }

        mGameboardFragment = new GameboardFragment();
        mPlayerHandFragment = new PlayerHandFragment();

        mPlayerHandFragment.setDelegate(this);

        if (mCreateGame) {
            switchToPlayerHand();
        } else {
            switchToGameboard();
        }
    }

    private void switchToPlayerHand() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contentFragment, mPlayerHandFragment);
        transaction.commit();
    }

    private void switchToGameboard() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contentFragment, mGameboardFragment);
        transaction.commit();
    }

    @Override
    public void onSignInFailed() {
        Toast.makeText(this, "Signin failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSignInSucceeded() {
        if (mCreateGame) {
            Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(getApiClient(), 1, 3, true);
            startActivityForResult(intent, SELECT_OPPONENTS);
        } else {
            Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(getApiClient());
            startActivityForResult(intent, LOOK_AT_MATCHES);
        }

        Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(), this);
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);

        if (request == LOOK_AT_MATCHES) {
            if (response != Activity.RESULT_OK) {
                finish();
                return;
            }

            TurnBasedMatch match = data.getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);
            if (match != null) {
                updateMatch(match);
            }
        } else if (request == SELECT_OPPONENTS) {
            if (response != Activity.RESULT_OK) {
                finish();
                return;
            }

            final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder().addInvitedPlayers(
                    invitees).build();

            Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc).setResultCallback(
                    initiateMatchResultResultCallback);
        }
    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch match) {
        updateMatch(match);
    }

    @Override
    public void onTurnBasedMatchRemoved(String matchId) {
        Toast.makeText(this, "A match was removed.", Toast.LENGTH_SHORT).show();
        // TODO: finish game

        showAlert("Warning", "Match removed!");
    }

    private ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> initiateMatchResultResultCallback =
            new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                @Override
                public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                    Toast.makeText(GameActivity.this, "A match was initiated.", Toast.LENGTH_SHORT).show();
                    String myParticipantId = result.getMatch().getParticipantId(Games.Players.getCurrentPlayerId(getApiClient()));

                    // If we are the creator, set up the game state
                    if (myParticipantId.equals(result.getMatch().getCreatorId())) {
                        // TODO: euchre
                        Game game = new CrazyEightsGame(result.getMatch(), Games.Players.getCurrentPlayerId(getApiClient()));
                        int j = 0;
                        for (Participant p : result.getMatch().getParticipants()) {
                            Player player = new Player();
                            player.setId(p.getParticipantId());
                            player.setName(p.getDisplayName());
                            player.setPosition(j++);

                            game.addPlayer(player);
                        }

                        game.setup();

                        Games.TurnBasedMultiplayer.takeTurn(getApiClient(), result.getMatch().getMatchId(), game.persist(), myParticipantId).setResultCallback(
                                new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                                    @Override
                                    public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                                        processResult(result);
                                    }
                                });;
                    }
                }
            };

    /**
     * Process the UpdateMatchResult
     */
    public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }

        updateMatch(match);
    }

    /**
     * Take the match and update the UI accordingly
     */
    public void updateMatch(TurnBasedMatch match) {
        mMatch = match;

        int status = match.getStatus();
        int turnStatus = match.getTurnStatus();

        // TODO: euchre
        Game game = new CrazyEightsGame(match, Games.Players.getCurrentPlayerId(getApiClient()));
        game.load(match.getData());

        switch (status) {
            case TurnBasedMatch.MATCH_STATUS_CANCELED:
                // TODO: implement this
                showAlert("Warning", "Match cancelled");
                return;
            case TurnBasedMatch.MATCH_STATUS_EXPIRED:
                // TODO: implement this
                showAlert("Warning", "Match expired");
                return;
            case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
                // TODO: implement this
                showAlert("Warning", "Match auto-matching");
                return;
            case TurnBasedMatch.MATCH_STATUS_COMPLETE:
                if (turnStatus != TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
                    Games.TurnBasedMultiplayer.finishMatch(getApiClient(), mMatch.getMatchId());
                }

                Intent winner = new Intent(this, GameResultsActivity.class);
                winner.putExtra(GameResultsActivity.IS_WINNER, game.getSelf().getCards().isEmpty());
                startActivityForResult(winner, QUIT_GAME);

                return;
        }

        switch (turnStatus) {
            case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
                switchToPlayerHand();
                break;
            case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
                switchToGameboard();
                break;
        }

        // Update both the gameboard and the player hand
        mPlayerHandFragment.updateGame(game);
        mGameboardFragment.updateUi(game);
    }

    private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
        mMatch = match;
        switch (statusCode) {
            case GamesStatusCodes.STATUS_OK:
                return true;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
                // This is OK; the action is stored by Google Play Services and will
                // be dealt with later.
                if (Util.isDebugBuild()) {
                    Toast.makeText(this, "Stored action for later.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                showAlert("Warning", getResources().getString(R.string.status_multiplayer_error_not_trusted_tester));
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
                showAlert("Warning", getResources().getString(R.string.match_error_already_rematched));
                break;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
                showAlert("Warning", getResources().getString(R.string.network_error_operation_failed));
                break;
            case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
                showAlert("Warning", getResources().getString(R.string.client_reconnect_required));
                break;
            case GamesStatusCodes.STATUS_INTERNAL_ERROR:
                showAlert("Warning", getResources().getString(R.string.internal_error));
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
                showAlert("Warning", getResources().getString(R.string.match_error_inactive_match));
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
                showAlert("Warning", getResources().getString(R.string.match_error_locally_modified));
                break;
            default:
                showAlert("Warning", getResources().getString(R.string.unexpected_status));
                Log.d(TAG, "Did not have warning or string to deal with: " + statusCode);
        }

        return false;
    }

    @Override
    public void gameUpdated(Game game) {
        Player self = game.getSelf();

        int nextPlayer = -1;
        for (int i = 0; i < game.getNumPlayers(); i++) {
            Player p = game.getPlayers().get(i);
            if (p.getId().equals(self.getId())) {
                nextPlayer = i + 1;
                break;
            }
        }

        if (nextPlayer >= game.getNumPlayers()) {
            nextPlayer = 0;
        }

        if (game.isGameOver()) {
            Games.TurnBasedMultiplayer.finishMatch(getApiClient(), mMatch.getMatchId(), game.persist()).setResultCallback(
                    new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                        @Override
                        public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                            processResult(result);
                        }
                    });
        } else {
            Games.TurnBasedMultiplayer.takeTurn(getApiClient(), mMatch.getMatchId(), game.persist(),
                    mMatch.getParticipants().get(nextPlayer).getParticipantId()).setResultCallback(
                    new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                        @Override
                        public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                            processResult(result);
                        }
                    });
        }
    }
}

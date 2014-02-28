package com.worthwhilegames.cardgames;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayerListener;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsGame;
import com.worthwhilegames.cardgames.crazyeights.GameboardFragment;
import com.worthwhilegames.cardgames.crazyeights.PlayerHandFragment;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.game.BaseGameActivity;

import java.util.ArrayList;

/**
 * Created by breber on 2/2/14.
 */
public class GameActivity extends BaseGameActivity implements
        TurnBasedMultiplayerListener, PlayerHandFragment.GameUpdatedListener {

    public static String CREATE_GAME_EXTRA = "CREATEGAME";

    public static final String TAG = GameActivity.class.getSimpleName();

    private GameboardFragment mGameboardFragment;
    private PlayerHandFragment mPlayerHandFragment;

    private TurnBasedMatch mMatch;
    private String mParticipantId;
    private boolean mIsDoingTurn = false;

    private boolean mCreateGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        mCreateGame = i.getBooleanExtra(CREATE_GAME_EXTRA, false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        if (Util.isDebugBuild()) {
            enableDebugLog(true, "BaseGame");
        }

        mGameboardFragment = new GameboardFragment();
        mPlayerHandFragment = new PlayerHandFragment();

        switchToGameboard();
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

    // TODO: look through this

    // For our intents
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_LOOK_AT_MATCHES = 10001;

    @Override
    public void onSignInSucceeded() {
        if (mCreateGame) {
            Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(getApiClient(), 1, 4, true);
            startActivityForResult(intent, RC_SELECT_PLAYERS);
        } else {
            Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(getApiClient());
            startActivityForResult(intent, RC_LOOK_AT_MATCHES);
        }

        switchToGameboard();
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);

        if (request == RC_LOOK_AT_MATCHES) {
            if (response != Activity.RESULT_OK) {
                // user canceled
                finish();
                return;
            }

            TurnBasedMatch match = data.getParcelableExtra(GamesClient.EXTRA_TURN_BASED_MATCH);

            if (match != null) {
                onTurnBasedMatchUpdated(GamesClient.STATUS_OK, match);
            }

            Log.d(TAG, "Match = " + match);
        } else if (request == RC_SELECT_PLAYERS) {
            // Returned from 'Select players to Invite' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
                finish();
                return;
            }

            // get the invitee list
            final ArrayList<String> invitees = data
                    .getStringArrayListExtra(GamesClient.EXTRA_PLAYERS);

            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees).build();

            Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc);
        }
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        Toast.makeText(this, "An invitation has arrived from " + invitation.getInviter().getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvitationRemoved(String s) {
        Toast.makeText(this, "An invitation was removed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch match) {
        Toast.makeText(this, "A match was updated.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTurnBasedMatchRemoved(String matchId) {
        Toast.makeText(this, "A match was removed.", Toast.LENGTH_SHORT).show();
    }

    // TODO: End look through this


    @Override
    public void onTurnBasedMatchCanceled(int i, String s) {
        if (!checkStatusCode(null, i)) {
            return;
        }

        showAlert("Match", "This match is canceled.  All other players will have their game ended.");
    }

    @Override
    public void onTurnBasedMatchInitiated(int i, TurnBasedMatch turnBasedMatch) {
        Toast.makeText(this, "A match was initiated.", Toast.LENGTH_SHORT).show();
        mParticipantId = turnBasedMatch.getParticipantId(Games.Players.getCurrentPlayerId(getApiClient()));

        // If we are the creator, set up the game state
        if (mParticipantId.equals(turnBasedMatch.getCreatorId())) {
            // TODO: euchre
            Game game = new CrazyEightsGame();
            int j = 0;
            for (Participant p : turnBasedMatch.getParticipants()) {
                Player player = new Player();
                player.setId(p.getParticipantId());
                player.setName(p.getDisplayName());
                player.setPosition(j++);

                game.addPlayer(player);
            }

            game.setup();

            Games.TurnBasedMultiplayer.takeTurn(getApiClient(), turnBasedMatch.getMatchId(), game.persist(), mParticipantId);
        }

        mPlayerHandFragment.setCurrentPlayerId(mParticipantId);

        onTurnBasedMatchUpdated(GamesClient.STATUS_OK, turnBasedMatch);
    }

    @Override
    public void onTurnBasedMatchLeft(int i, TurnBasedMatch turnBasedMatch) {
        Toast.makeText(this, "A match was left.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTurnBasedMatchUpdated(int i, TurnBasedMatch turnBasedMatch) {
        Toast.makeText(this, "A match was updated.", Toast.LENGTH_SHORT).show();
        if (!checkStatusCode(turnBasedMatch, i)) {
            return;
        }

        mIsDoingTurn = (turnBasedMatch.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

        // TODO: euchre
        Game game = new CrazyEightsGame();
        game.load(turnBasedMatch.getData());
        if (mIsDoingTurn) {
            switchToPlayerHand();
            mPlayerHandFragment.updateGame(game);
        } else {
            switchToGameboard();
            mGameboardFragment.updateUi(game);
        }
    }

    @Override
    public void onTurnBasedMatchesLoaded(int i, LoadMatchesResponse loadMatchesResponse) {
        Toast.makeText(this, "A match was loaded.", Toast.LENGTH_SHORT).show();
    }

    public void showErrorMessage(TurnBasedMatch match, int statusCode,
                                 int stringId) {

        showAlert("Warning", getResources().getString(stringId));
    }

    private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
        mMatch = match;
        switch (statusCode) {
            case GamesClient.STATUS_OK:
                return true;
            case GamesClient.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
                // This is OK; the action is stored by Google Play Services and will
                // be dealt with later.
                if (Util.isDebugBuild()) {
                    Toast.makeText(this, "Stored action for later.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                showErrorMessage(match, statusCode,
                        R.string.status_multiplayer_error_not_trusted_tester);
                break;
            case GamesClient.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
                showErrorMessage(match, statusCode,
                        R.string.match_error_already_rematched);
                break;
            case GamesClient.STATUS_NETWORK_ERROR_OPERATION_FAILED:
                showErrorMessage(match, statusCode,
                        R.string.network_error_operation_failed);
                break;
            case GamesClient.STATUS_CLIENT_RECONNECT_REQUIRED:
                showErrorMessage(match, statusCode,
                        R.string.client_reconnect_required);
                break;
            case GamesClient.STATUS_INTERNAL_ERROR:
                showErrorMessage(match, statusCode, R.string.internal_error);
                break;
            case GamesClient.STATUS_MATCH_ERROR_INACTIVE_MATCH:
                showErrorMessage(match, statusCode,
                        R.string.match_error_inactive_match);
                break;
            case GamesClient.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
                showErrorMessage(match, statusCode,
                        R.string.match_error_locally_modified);
                break;
            default:
                showErrorMessage(match, statusCode, R.string.unexpected_status);
                Log.d(TAG, "Did not have warning or string to deal with: "
                        + statusCode);
        }

        return false;
    }

    @Override
    public void gameUpdated(Game game) {
        int nextPlayer = -1;
        for (int i = 0; i < game.getNumPlayers(); i++) {
            Player p = game.getPlayers().get(i);
            if (p.getId().equals(mParticipantId)) {
                nextPlayer = i + 1;
                break;
            }
        }

        if (nextPlayer >= game.getNumPlayers()) {
            nextPlayer = 0;
        }

        Games.TurnBasedMultiplayer.takeTurn(getApiClient(), mMatch.getMatchId(), game.persist(), mMatch.getParticipants().get(nextPlayer).getParticipantId());
    }
}

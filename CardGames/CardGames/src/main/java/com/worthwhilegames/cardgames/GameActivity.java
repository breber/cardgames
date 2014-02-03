package com.worthwhilegames.cardgames;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
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

    public static final String TAG = GameActivity.class.getSimpleName();

    private GameboardFragment mGameboardFragment;
    private PlayerHandFragment mPlayerHandFragment;

    // This is the current match we're in; null if not loaded
    public TurnBasedMatch mMatch;

    private String mParticipantId;
    private boolean mIsDoingTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        if (Util.isDebugBuild()) {
            enableDebugLog(true, "BaseGame");
        }

        mGameboardFragment = new GameboardFragment();
        mPlayerHandFragment = new PlayerHandFragment();

        switchToPlayerHand();
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
//        finish();
    }

    // TODO: look through this

    // For our intents
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_LOOK_AT_MATCHES = 10001;

    @Override
    public void onSignInSucceeded() {
        if (mHelper.getTurnBasedMatch() != null) {
            // GameHelper will cache any connection hint it gets. In this case,
            // it can cache a TurnBasedMatch that it got from choosing a turn-based
            // game notification. If that's the case, you should go straight into
            // the game.
//            updateMatch(mHelper.getTurnBasedMatch());
            return;
        }

        switchToPlayerHand();

        // As a demonstration, we are registering this activity as a handler for
        // invitation and match events.

        // This is *NOT* required; if you do not register a handler for
        // invitation events, you will get standard notifications instead.
        // Standard notifications may be preferable behavior in many cases.
        getGamesClient().registerInvitationListener(this);

        // Likewise, we are registering the optional MatchUpdateListener, which
        // will replace notifications you would get otherwise. You do *NOT* have
        // to register a MatchUpdateListener.
        getGamesClient().registerMatchUpdateListener(this);
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        // It's VERY IMPORTANT for you to remember to call your superclass.
        // BaseGameActivity will not work otherwise.
        super.onActivityResult(request, response, data);

        if (request == RC_LOOK_AT_MATCHES) {
            // Returning from the 'Select Match' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            TurnBasedMatch match = data
                    .getParcelableExtra(GamesClient.EXTRA_TURN_BASED_MATCH);

//            if (match != null) {
//                updateMatch(match);
//            }

            Log.d(TAG, "Match = " + match);
        } else if (request == RC_SELECT_PLAYERS) {
            // Returned from 'Select players to Invite' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            // get the invitee list
            final ArrayList<String> invitees = data
                    .getStringArrayListExtra(GamesClient.EXTRA_PLAYERS);

            // get automatch criteria
            Bundle autoMatchCriteria = null;

            int minAutoMatchPlayers = data.getIntExtra(
                    GamesClient.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers = data.getIntExtra(
                    GamesClient.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }

            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees)
                    .setAutoMatchCriteria(autoMatchCriteria).build();

            // Kick the match off
            getGamesClient().createTurnBasedMatch(this, tbmc);
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
        mMatch = turnBasedMatch;
        mParticipantId = mMatch.getParticipantId(getGamesClient().getCurrentPlayerId());

        // If we are the creator, set up the game state
        if (mParticipantId == mMatch.getCreatorId()) {
            // TODO: euchre
            Game game = new CrazyEightsGame();
            int j = 0;
            for (Participant p : mMatch.getParticipants()) {
                Player player = new Player();
                player.setId(p.getParticipantId());
                player.setName(p.getDisplayName());
                player.setPosition(j++);

                game.addPlayer(player);
            }

            game.setup();

            mGameboardFragment.updateUi(game);

            getGamesClient().takeTurn(this, mMatch.getMatchId(), game.persist(), mParticipantId);
        }

        mPlayerHandFragment.setCurrentPlayerId(mParticipantId);;
    }

    @Override
    public void onTurnBasedMatchLeft(int i, TurnBasedMatch turnBasedMatch) {

    }

    @Override
    public void onTurnBasedMatchUpdated(int i, TurnBasedMatch turnBasedMatch) {
        if (!checkStatusCode(turnBasedMatch, i)) {
            return;
        }

        mIsDoingTurn = (mMatch.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

        // TODO: euchre
        Game game = new CrazyEightsGame();
        game.load(turnBasedMatch.getData());
        if (mIsDoingTurn) {
            mPlayerHandFragment.updateGame(game);
            switchToPlayerHand();
        } else {
            mGameboardFragment.updateUi(game);
            switchToGameboard();
        }
    }

    @Override
    public void onTurnBasedMatchesLoaded(int i, LoadMatchesResponse loadMatchesResponse) {

    }

    public void showErrorMessage(TurnBasedMatch match, int statusCode,
                                 int stringId) {

        showAlert("Warning", getResources().getString(stringId));
    }

    private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
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

        getGamesClient().takeTurn(this, mMatch.getMatchId(), game.persist(), mMatch.getParticipants().get(nextPlayer).getParticipantId());
    }
}

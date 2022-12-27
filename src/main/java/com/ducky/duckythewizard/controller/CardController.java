package com.ducky.duckythewizard.controller;

import com.ducky.duckythewizard.model.Game;
import com.ducky.duckythewizard.model.Stone;
import com.ducky.duckythewizard.model.card.Card;
import com.ducky.duckythewizard.model.card.CardDeck;
import com.ducky.duckythewizard.model.config.GameConfig;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class CardController extends Controller {
    private ArrayList<Card> handCards = this.getSession().getPlayer().getHandCards();
    private ArrayList<EventHandler<MouseEvent>> clickHandlers = new ArrayList<>();
    private CardDeck deck = this.getSession().getCardDeck();

    public CardController (Game game) {
        super(game);
    }

    public Card clickedCard(MouseEvent event) {
        int clickedCardPosition = this.deck.getHandCardPosition(event);
        Card clickedCard = this.handCards.get(clickedCardPosition);
        if (!clickedCard.getColor().getName().equals("none")) {
            return clickedCard;
        } else {
            return null;
        }
    }

    public void cardInit() {
        this.getSession().getCardDeck().renderAllHandCardImages(this.getSession().getPlayer().getHandCards(), this.getSession().getAnchorPaneCards());
    }

    public void removeCardFromHandCardsAddDummy(int clickedCardPosition) {
        this.handCards.add(clickedCardPosition, this.deck.removeHandCard(clickedCardPosition, this.handCards, false));
        this.deck.renderEmptyCard((ImageView) this.getSession().getAnchorPaneCards().getChildren().get(clickedCardPosition));
    }
    public void addMouseEventHandler() {
        for (Node node : this.getSession().getAnchorPaneCards().getChildren()) {
            ImageView imgView = (ImageView) node;
            EventHandler<MouseEvent> cardClicked = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // take focus, so key's can be pressed
                    getSession().getFightView().getAnchorPaneFightOverlay().requestFocus();
                    if(!getSession().getIsRunning()) {
                        Card clickedCard = clickedCard(mouseEvent);

                        if (clickedCard != null) {
                            // if ducky played first, the stone card needs to be rendered after clicking a card
                            if (getSession().getActiveFight().getDuckyPlaysFirst()) {
                                getSession().getFightView().renderFightViewCard(false);
                            }

                            int clickPosition = getSession().getCardDeck().getHandCardPosition(mouseEvent);
                            // give clicked card to fight-class
                            getSession().getActiveFight().setDuckyCard(clickedCard);
                            // render an empty card-image at clicked card position
                            getSession().getCardCtrl().removeCardFromHandCardsAddDummy(clickPosition);
                            // render clicked card to fight-scene
                            getSession().getFightView().renderFightViewCard(true);

                            // determine Winner
                            // set win- or loss-label
                            boolean duckyWin = getSession().getActiveFight().determineWinner();
                            getSession().getFightView().updateWinLossLabel(duckyWin);

                            removeAllClickHandlers();

                            if (checkWin(duckyWin).equals("win") || checkWin(duckyWin).equals("loss")) {
                                getSession().getGameCtrl().renderEndScene(duckyWin);
                            }

                            // END FIGHT:
                            // end fight-view clicking x-Label
                            getSession().getFightView().addExitLabel();
                            getSession().getFightView().getExitLabel().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent1 -> {
                                endFight(clickPosition, duckyWin);
                            });
                            // end fight-view clicking ESC
                            getSession().getFightView().getAnchorPaneFightOverlay().setOnKeyPressed(keyEvent -> {
                                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                                    endFight(clickPosition, duckyWin);
                                }
                            });
                        }
                    }
                }
            };
            clickHandlers.add(cardClicked);
            imgView.addEventFilter(MouseEvent.MOUSE_CLICKED, cardClicked);
        }
    }

    private String checkWin(boolean duckyWin) {
        // TODO just if Ducky won:
        // after fight: deal new cards, check ducky win/loss
        int cardsLeft = getSession().getCardDeck().getCardDeck().size();
        if (duckyWin && cardsLeft <= 2) {
            return "win";
        } else if (!duckyWin) {
            if (cardsLeft >= 2) {
                return getSession().getPlayer().checkAvailableCards() ? "no" : "loss";
            } else {
                return getSession().getPlayer().checkAvailableCards() ? "loss" : "no";
            }
        }
        return "no";
    }

    private void endFight(int clickPosition, boolean duckyWin) {
        getSession().getGameCtrl().endCollision();
        newCardsFromDeck(clickPosition, duckyWin);
/*        System.out.println("card deck size: " + this.getSession().getCardDeck().getCardDeck().size());
        System.out.println("playable cards " + this.getSession().getPlayer().checkAvailableCards());*/
    }

    private void newCardsFromDeck(int clickedPosition, boolean duckyWin) {
        // first take one new card from deck, give to hand-cards at clicked hand-card-position
        // render new card given from deck
        // new card for ducky
        if (duckyWin) {
            this.handCards.add(clickedPosition, this.deck.removeHandCard(clickedPosition, this.handCards, true));
        } else {
            this.handCards.add(clickedPosition, this.deck.removeHandCard(clickedPosition, this.handCards, false));
            this.getSession().getPlayer().decrementHandCards();
        }
        this.deck.renderCard(clickedPosition, this.handCards, this.getSession().getAnchorPaneCards());
        // new card for stone
        this.getSession().getActiveFight().getStoneInFight().setCard(this.deck.dealOneNewCardFromDeck());
    }

    private void removeAllClickHandlers() {
        for (int i = 0; i < GameConfig.AMOUNT_HAND_CARDS; i++){
            ImageView imgView = (ImageView) this.getSession().getAnchorPaneCards().getChildren().get(i);
            imgView.removeEventFilter(MouseEvent.MOUSE_CLICKED, clickHandlers.get(i));
        }
        clickHandlers.clear();
    }

    public void addCardToStones() {
        for (Stone stone : this.getSession().getStoneArrayList()) {
            stone.setCard(this.deck.dealOneNewCardFromDeck());
        }
    }
}

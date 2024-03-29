package com.ducky.duckythewizard.model;

import com.ducky.duckythewizard.model.card.Card;
import java.util.Random;

/**In this class all objects relevant to the current fight are saved (the stone Ducky is fighting against,
 * which cards have been played in the fight, what colour is trump etc. - all collected facts of the fight
 * are then used to determine who has won.*/

public class Fight {

    private Stone stoneInFight;
    private Card playerCard;
    private Card stoneCard;
    private String trump;
    private final boolean playerStart;

    public Fight() {
        // determine who starts
        this.playerStart = playerStarts();
    }


    public void setStoneCard(Card stoneCard) { this.stoneCard = stoneCard; }

    public void setPlayerCard(Card playerCard) { this.playerCard = playerCard; }

    public void setStoneInFight(Stone stoneInFight) { this.stoneInFight = stoneInFight; }

    public void setTrump(Stone stone) { this.trump = stone.getRandomTrumpColorStone(); }

    public Card getPlayerCard() { return this.playerCard; }

    public Card getStoneCard() { return this.stoneCard; }

    public Stone getStoneInFight() { return this.stoneInFight; }

    public boolean getPlayerStart() { return this.playerStart; }

    private boolean playerStarts() { return new Random().nextBoolean(); }

    /**This method check's if the player wins the fight according to the rules set by the developer ladies.
     * The boolean result is used elsewhere to determine which screen to show to the player (win/loss of fight).*/
    public boolean determineIfDuckyIsWinner() {
        int stoneScore = stoneCard.getValue();
        int playerScore = playerCard.getValue();

        // if both got wizard, first wizard wins
        if (playerScore == 199 && stoneScore == 199) {
            if (this.playerStart) {
                playerScore += 1;
            } else
                stoneScore += 1;
        } else {
            // if no wizard's was played, check if someone has trump-color:
            if (this.playerCard.getColor().getName().equals(this.trump)) {
                playerScore += 100;
            }
            if (this.stoneCard.getColor().getName().equals(this.trump)) {
                stoneScore += 100;
            }
            // if no one had trump color:
            if ((!this.playerCard.getColor().getName().equals(trump)) && (!this.stoneCard.getColor().getName().equals(trump))) {
                // the one, that started gets +100
                if (this.playerStart) {
                    playerScore += 100;
                    if (this.stoneCard.getColor().getName().equals(this.playerCard.getColor().getName())) {
                        // if stone has same color, gets also +100
                        stoneScore += 100;
                    }
                } else {
                    stoneScore += 100;
                    if (this.playerCard.getColor().getName().equals(this.stoneCard.getColor().getName())) {
                        // if ducky has same color, gets also +100
                        playerScore += 100;
                    }
                }
            }
        }
        // determining who has higher score and triggering message
        return playerScore > stoneScore;
    }

    /**This method calculates how many points Ducky has earned according to if he won and with what card in what setting.
     *
     * @param playerWin informs if Ducky has won the fight*/
    public int calculateScore(boolean playerWin) {
        int score = 0;

        if (playerWin) {
            //each win earns 20 points
            score += 20;

            int duckyCardValue = this.playerCard.getValue() + 1;

            if (this.playerStart) {
                //if you've won without knowing the stone's card you get extra points
                score += 5;
            }

            if (this.trump.equals("none")) {
                //winning a no trump round earns extra points
                score += 15;
            }

            //if you've won without a wizard or trump, you can earn more points the closer your card value is to the stone card value
            if ((this.playerCard.getValue() != 199) && (!this.playerCard.getColor().getName().equals(this.trump))) {
                int difference = this.playerCard.getValue() - this.stoneCard.getValue();

                if (difference < 4) {
                    score += duckyCardValue * 2;
                }
                else {
                    if (difference < duckyCardValue) {
                        score += duckyCardValue - difference;
                    }
                }
            }

        }
        else {
            //each lost fight causes loss of 10 points
            score -= 10;
        }

        return score;
    }
}

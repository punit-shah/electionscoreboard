package punitshah.electionscoreboard.scoreboard;

import punitshah.electionscoreboard.scoreboard.view.ScoreboardView;

public class Main {
    public static void main(String[] args) {
        try {
            new ScoreboardView().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

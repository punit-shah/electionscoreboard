package uk.co.bbc.datasystems.electionscoreboard;

import uk.co.bbc.datasystems.electionscoreboard.view.ScoreboardView;

public class Main {
    public static void main(String[] args) {
        try {
            new ScoreboardView().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

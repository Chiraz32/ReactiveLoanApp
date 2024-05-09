package services;

import models.ScoreResult;

import java.util.Random;

public class RiskManagementService {
    ScoreResult initialScoreResult =new ScoreResult(0,false);
    ScoreResult finalScoreResult =new ScoreResult(0,false);

    public ScoreResult calculateInitialScore() {
        System.out.println("Score is being calculated");
        double x = new Random().nextDouble() * 100;
        return new ScoreResult((int) x, x > 100);
    }

    public void UpdateLoanDetails (){
        if (this.finalScoreResult.status)
        {
            System.out.println("The Loan's request is accepted");
        }
        else {
            System.out.println("The Loan's request is refused");
        }
    }
}

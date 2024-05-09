package services;

import models.ScoreResult;

import java.util.Random;

public class CommercialService {
    ScoreResult scoreResult =new ScoreResult(0,false);

    public ScoreResult calculateInitialScore() {
        System.out.println("Score is being calculated");
        double x = new Random().nextDouble() * 100;
        return new ScoreResult((int) x, x > 100);
    }

    public void UpdateLoanDetails (){
        if (this.scoreResult.status)
        {
            System.out.println("Loan details are saved and the Loan's request  is still pending");
        }
        else {
            System.out.println("loan's request is refused");
        }
    }
}

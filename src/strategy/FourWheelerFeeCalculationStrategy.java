package strategy;

public class FourWheelerFeeCalculationStrategy implements FeeCalculationStrategy {
    private static final double BASE=20;
    private static final double Hourly_RATE=10;


    @Override
    public double calculateFee(long durationInMinutes) {

        if(durationInMinutes<=60){
            return BASE;
        }
        return BASE+((durationInMinutes-60)/60)*Hourly_RATE;

    }
}

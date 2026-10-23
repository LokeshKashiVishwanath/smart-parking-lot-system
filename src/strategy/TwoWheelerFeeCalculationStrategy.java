package strategy;

public class TwoWheelerFeeCalculationStrategy implements FeeCalculationStrategy {

    private static final double BASE_FEE = 10.0; // Base fee for the first hour
    private static final double HOURLY_RATE = 5.0; // Hourly rate after the first hour

    @Override
    public double calculateFee(long durationInMinutes) {
        if(durationInMinutes<60){
            return BASE_FEE;

        }
        return BASE_FEE+((durationInMinutes - 60) / 60.0) * HOURLY_RATE;
    }
}

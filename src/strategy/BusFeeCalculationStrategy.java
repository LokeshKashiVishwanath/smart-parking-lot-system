package strategy;

public class BusFeeCalculationStrategy implements FeeCalculationStrategy {
    private static final double BASE_FEE = 30.0;
    private static final double HOURLY_RATE = 15.0;

    @Override
    public double calculateFee(long durationInMinutes) {
        if (durationInMinutes <= 60) {
            return BASE_FEE;
        }
        return BASE_FEE + ((durationInMinutes - 60) / 60.0) * HOURLY_RATE;
    }
}


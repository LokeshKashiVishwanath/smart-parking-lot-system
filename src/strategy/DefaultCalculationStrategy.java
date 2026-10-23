package strategy;

public class DefaultCalculationStrategy implements  FeeCalculationStrategy{

    private final static double defaultFare=10;
    @Override
    public double calculateFee(long durationInMinutes) {
      return defaultFare * durationInMinutes/60;
    }
}

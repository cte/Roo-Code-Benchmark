import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PythagoreanTriplet {
    private final int a;
    private final int b;
    private final int c;

    PythagoreanTriplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PythagoreanTriplet that = (PythagoreanTriplet) o;
        return a == that.a && b == that.b && c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", a, b, c);
    }

    static TripletListBuilder makeTripletsList() {
        return new TripletListBuilder();
    }

    static class TripletListBuilder {
        private int sum;
        private int maxFactor = Integer.MAX_VALUE;

        TripletListBuilder thatSumTo(int sum) {
            this.sum = sum;
            return this;
        }

        TripletListBuilder withFactorsLessThanOrEqualTo(int maxFactor) {
            this.maxFactor = maxFactor;
            return this;
        }

        List<PythagoreanTriplet> build() {
            List<PythagoreanTriplet> triplets = new ArrayList<>();
            
            // For a Pythagorean triplet, a < b < c and a + b + c = sum
            // Also, a² + b² = c²
            // We can derive: c = sum - a - b
            // And substitute: a² + b² = (sum - a - b)²
            
            // The minimum value for a is 3 (smallest possible in a Pythagorean triplet)
            // The maximum value for a is (sum - 3) / 3 because a < b < c and a + b + c = sum
            int aMax = Math.min((sum - 3) / 3, maxFactor);
            
            for (int a = 3; a <= aMax; a++) {
                // For a given a, we can calculate the value of b using the formula:
                // a² + b² = (sum - a - b)²
                // a² + b² = sum² - 2*sum*a - 2*sum*b + a² + 2*a*b + b²
                // 0 = sum² - 2*sum*a - 2*sum*b + 2*a*b
                // 2*sum*b - 2*a*b = sum² - 2*sum*a
                // b(2*sum - 2*a) = sum² - 2*sum*a
                // b = (sum² - 2*sum*a) / (2*sum - 2*a)
                // b = (sum - a) * sum / (2 * (sum - a))
                // b = sum / 2
                
                // However, this is a simplification. We need to ensure b is an integer.
                // Let's use a more direct approach:
                
                // The minimum value for b is a + 1
                // The maximum value for b is (sum - a - 1) / 2 because b < c and a + b + c = sum
                int bMax = Math.min((sum - a - 1) / 2, maxFactor);
                
                for (int b = a + 1; b <= bMax; b++) {
                    int c = sum - a - b;
                    
                    // Check if c is within the maxFactor constraint
                    if (c > maxFactor) {
                        continue;
                    }
                    
                    // Check if it's a Pythagorean triplet: a² + b² = c²
                    if (a * a + b * b == c * c) {
                        triplets.add(new PythagoreanTriplet(a, b, c));
                    }
                }
            }
            
            return triplets;
        }
    }
}
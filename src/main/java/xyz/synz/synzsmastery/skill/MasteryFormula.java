package xyz.synz.synzsmastery.skill;

/**
 * Generic diminishing-returns curve used to turn a lifetime activity count
 * into a small bonus that approaches (but never reaches) a cap.
 *
 * Formula: bonus = cap * (count / (count + halfLife)) ^ steepness
 * - At count = 0, bonus = 0.
 * - As count grows without bound, bonus approaches cap but never exceeds it.
 * - steepness = 1.0 gives fast early growth that levels off quickly.
 * - steepness < 1.0 (e.g. 0.5) slows early growth and stretches gains out
 *   over a much wider range — good for skills that should feel "grindy."
 */
public final class MasteryFormula {

    private MasteryFormula() {
    }

    public static double diminishingReturns(long lifetimeCount, double cap, double halfLife, double steepness) {
        if (lifetimeCount <= 0) {
            return 0.0;
        }

        double ratio = lifetimeCount / (double) (lifetimeCount + halfLife);
        return cap * Math.pow(ratio, steepness);
    }
}
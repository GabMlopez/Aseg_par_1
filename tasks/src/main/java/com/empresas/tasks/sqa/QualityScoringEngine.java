package com.empresas.tasks.sqa;

public class QualityScoringEngine {
    private static final double WEIGHT_CORRECTNESS =0.3;
    private static final double WEIGHT_TESTABILITY =0.3;
    private static final double WEIGHT_MAINTAINABILITY =0.3;
    private static final double WEIGHT_INTEGRITY =0.3;

    public static double calculateScore(double coveragePercentage, int bugs, int smells, int vulns){
        double testabilityScore  = Math.min(coveragePercentage, 100.00);
        double correctnessScore =Math.max(100.00 - (bugs*10),0);
        double maintainabilityScore = Math.max(100.00 -(smells*2),0);
        double integrityScore = vulns == 0? 100.00 : 0.00;

        return (testabilityScore * WEIGHT_TESTABILITY) +
                (correctnessScore * WEIGHT_CORRECTNESS) +
                (maintainabilityScore * WEIGHT_MAINTAINABILITY) +
                (integrityScore * WEIGHT_INTEGRITY);
    }
}

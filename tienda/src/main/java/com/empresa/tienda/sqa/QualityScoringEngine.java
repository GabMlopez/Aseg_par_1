package com.empresa.tienda.sqa;

import org.springframework.stereotype.Component;

@Component
public class QualityScoringEngine {
    private static final double WEIGHT_CORRECTNESS = 0.3;
    private static final double WEIGHT_TESTABILITY = 0.3;
    private static final double WEIGHT_MAINTAINABILITY = 0.2;
    private static final double WEIGHT_INTEGRITY = 0.2;

    public double calculateScore(double coveragePercent, int bugs, int smells, int vulns) {
        double testabilityScore = Math.min(coveragePercent, 100.0);
        double correctnessScore = Math.max(100.0 - (bugs * 10), 0);
        double maintainabilityScore = Math.max(100.0 - (smells * 2), 0);
        double integrityScore = vulns == 0 ? 100.0 : 0.0;

        return (testabilityScore * WEIGHT_TESTABILITY) +
                (correctnessScore * WEIGHT_CORRECTNESS) +
                (maintainabilityScore * WEIGHT_MAINTAINABILITY) +
                (integrityScore * WEIGHT_INTEGRITY);
    }
}
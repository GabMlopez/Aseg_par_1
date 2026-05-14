package com.empresa.tienda;

import com.empresa.tienda.sqa.QualityScoringEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QualityScoringEngine - Pruebas Unitarias")
class QualityScoringEngineTest {

    private QualityScoringEngine engine;

    @BeforeEach
    void setUp() {
        engine = new QualityScoringEngine();
    }

    /*
     * Fórmula:
     *   testability   = min(coverage, 100)    × 0.30
     *   correctness   = max(100 - bugs×10, 0) × 0.30
     *   maintainability = max(100 - smells×2, 0) × 0.20
     *   integrity     = (vulns==0 ? 100 : 0)   × 0.20
     */

    // -------------------------------------------------------------------------
    // TC-QS-01: Score perfecto (100% cobertura, sin bugs/smells/vulns)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-01: Score perfecto – cobertura 100%, sin defectos")
    void shouldReturnMaxScoreForPerfectMetrics() {
        // 30 + 30 + 20 + 20 = 100
        double score = engine.calculateScore(100.0, 0, 0, 0);
        assertEquals(100.0, score, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-02: Score cero (0% cobertura, máximos bugs/smells/vulns)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-02: Score mínimo – cobertura 0%, 10 bugs, 50 smells, 1 vuln")
    void shouldReturnZeroScoreForWorstMetrics() {
        // testability=0, correctness=max(100-100,0)=0, maint=max(100-100,0)=0, integrity=0
        double score = engine.calculateScore(0.0, 10, 50, 1);
        assertEquals(0.0, score, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-03: Score parcial – cobertura 85%, 1 bug, 5 smells, sin vulns
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-03: Score parcial – cobertura 85%, 1 bug, 5 smells, 0 vulns")
    void shouldCalculatePartialScore() {
        // testability   = 85 × 0.30 = 25.5
        // correctness   = (100-10) × 0.30 = 90 × 0.30 = 27.0
        // maintainability = (100-10) × 0.20 = 90 × 0.20 = 18.0
        // integrity     = 100 × 0.20 = 20.0
        // total = 90.5
        double score = engine.calculateScore(85.0, 1, 5, 0);
        assertEquals(90.5, score, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-04: Vulnerabilidad presente reduce integrity a 0
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-04: Con 1+ vulnerabilidades, la puntuación de integridad es 0")
    void shouldZeroIntegrityWhenVulnsPresent() {
        // testability = 100×0.30=30, correctness=100×0.30=30, maint=100×0.20=20, integrity=0
        double score = engine.calculateScore(100.0, 0, 0, 1);
        assertEquals(80.0, score, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-05: Cobertura > 100 se trata como 100 (clamping)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-05: Cobertura mayor a 100 se limita a 100 (Math.min)")
    void shouldClampCoverageAt100() {
        double scoreNormal = engine.calculateScore(100.0, 0, 0, 0);
        double scoreOver   = engine.calculateScore(150.0, 0, 0, 0);
        assertEquals(scoreNormal, scoreOver, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-06: Correctness no es negativo aunque haya muchos bugs
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-06: Correctness nunca es negativo – 20 bugs resulta en 0")
    void shouldNotGoBelowZeroForCorrectnessWithManyBugs() {
        // testability   = 0   × 0.30 = 0
        // correctness   = max(100 - 200, 0) × 0.30 = 0
        // maintainability = max(100 - 0, 0)  × 0.20 = 20  ← 0 smells
        // integrity     = (vulns=1 → 0)      × 0.20 = 0
        // total = 20.0
        double score = engine.calculateScore(0.0, 20, 0, 1);
        assertEquals(20.0, score, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-07: Maintainability no es negativo con muchos code smells
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-07: Maintainability nunca es negativo – 100 smells resulta en 0")
    void shouldNotGoBelowZeroForMaintainabilityWithManySmells() {
        // maintainability = max(100 - 200, 0) = 0
        double score = engine.calculateScore(100.0, 0, 100, 0);
        // testability=30 + correctness=30 + maint=0 + integrity=20 = 80
        assertEquals(80.0, score, 0.001);
    }

    // -------------------------------------------------------------------------
    // TC-QS-08: Resultado del umbral mínimo del 85% (objetivo JaCoCo)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-QS-08: Con cobertura 85% y código limpio supera umbral de 80 puntos")
    void shouldPassMinimumThreshold() {
        double score = engine.calculateScore(85.0, 0, 0, 0);
        // 25.5 + 30 + 20 + 20 = 95.5
        assertTrue(score >= 80.0, "El score debe superar 80 puntos con buenas métricas");
    }
}

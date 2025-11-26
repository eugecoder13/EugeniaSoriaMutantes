package com.magneto.service;

import org.springframework.stereotype.Service;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        int n = dna.length;

        // Convertir a matriz de chars
        char[][] grid = new char[n][n];
        for (int i = 0; i < n; i++) {
            grid[i] = dna[i].toCharArray();
        }

        int sequencesFound = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                char base = grid[row][col];

                // Horizontal →
                if (col <= n - SEQUENCE_LENGTH &&
                        base == grid[row][col + 1] &&
                        base == grid[row][col + 2] &&
                        base == grid[row][col + 3]) {
                    sequencesFound++;
                }

                // Vertical ↓
                if (row <= n - SEQUENCE_LENGTH &&
                        base == grid[row + 1][col] &&
                        base == grid[row + 2][col] &&
                        base == grid[row + 3][col]) {
                    sequencesFound++;
                }

                // Diagonal ↘
                if (row <= n - SEQUENCE_LENGTH &&
                        col <= n - SEQUENCE_LENGTH &&
                        base == grid[row + 1][col + 1] &&
                        base == grid[row + 2][col + 2] &&
                        base == grid[row + 3][col + 3]) {
                    sequencesFound++;
                }

                // Diagonal ↙
                if (row <= n - SEQUENCE_LENGTH &&
                        col >= SEQUENCE_LENGTH - 1 &&
                        base == grid[row + 1][col - 1] &&
                        base == grid[row + 2][col - 2] &&
                        base == grid[row + 3][col - 3]) {
                    sequencesFound++;
                }

                // early termination: apenas haya 2+
                if (sequencesFound > 1) {
                    return true;
                }
            }
        }

        return false;
    }
}

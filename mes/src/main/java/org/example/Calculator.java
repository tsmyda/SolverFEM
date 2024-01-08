package org.example;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealVector;

import java.util.Collections;

import static java.lang.Math.*;

public class Calculator {
    private static final double G = 6.673e-11;
    //private static final double G=6.673e-1;
    //private static final double G=2;
    private static final double CONSTANT = 4*PI*G;
    private final IterativeLegendreGaussIntegrator Integrator = new IterativeLegendreGaussIntegrator(2, 1e-8, 1e-8);
    private Array2DRowRealMatrix GenerateMatrixB(int n, double h) {
        Array2DRowRealMatrix B =  new Array2DRowRealMatrix(n-1, n-1);
        double a = Integrator.integrate(Integer.MAX_VALUE, x->1/pow(h, 2), 0,2*h);
        double b = Integrator.integrate(Integer.MAX_VALUE, x->1/pow(h, 2), 0,h);
        for (int row = 0; row<n-1; row++) {
            for (int col = 0; col < n-1; col++) {
                double val=0;
                if (row == col) val = Integrator.integrate(Integer.MAX_VALUE, x->1/pow(h, 2), 0,2*h);
                if (row == col + 1)
                    val = Integrator.integrate(Integer.MAX_VALUE, x -> -1 / pow(h, 2), (col+1) * h, (row+1) * h);
                if (col == row + 1)
                    val = Integrator.integrate(Integer.MAX_VALUE, x -> -1 / pow(h, 2), (row+1) * h, (col+1) * h);
                B.setEntry(row, col, val);
            }
        }
        return B;
    }
    private boolean isIntersection(double a, double b, double c, double d) {
        if ((c<a && a<d) || (c<b && b<d)) return true;
        return false;
    }
    private ArrayRealVector GenerateMatrixL(int n, double h) {
        ArrayRealVector L = new ArrayRealVector(n-1);
        for (int i=0; i<n-1; i++) {
            double value = 0;
//            value += 1/3* Integrator.integrate(Integer.MAX_VALUE, x->1/h, h*i, h*(i+1));
//            value += 1/3* Integrator.integrate(Integer.MAX_VALUE, x->-1/h, h*(i+1), h*(i+2));
//            te calki wyzej sie zeruja jakby co okok ;)
//            System.out.println(value);
            if(isIntersection(h*i, h*(i+1), 1, 2)) {
                double lower = max(h*i, 1);
                double upper = min(h*(i+1), 2);
                int finalI = i;
                value -= CONSTANT * Integrator.integrate(Integer.MAX_VALUE, x -> x / h - finalI + 1, lower, upper);
            }
            if(isIntersection(h*(i+1), h*(i+2), 1, 2)) {
                double lower = max(h*(i+1), 1);
                double upper = min(h*(i+2), 2);
                int finalI1 = i;
                value -= CONSTANT * Integrator.integrate(Integer.MAX_VALUE, x -> -x / h + finalI1 + 1, lower, upper);
            }
//            almost sure it can be done this this way, but for some reason it does not work:
//            oh wait, actually it does but only if u pass the n argument lesser than 5; if u pass 5 or greater it crashes

//            int finalI = i;
//            value -= CONSTANT * Integrator.integrate(Integer.MAX_VALUE, x -> (x>= h*finalI && x<=h*(finalI+1)) ? x / h - finalI + 1 : 0, 1, 2);
//            value -= CONSTANT * Integrator.integrate(Integer.MAX_VALUE, x -> (x>=h*(finalI+1) && x<=(h*(finalI+2))) ? -x / h + finalI + 1 : 0, 1, 2);
            L.setEntry(i, value);
        }
        return L;
    }
    private double[] CalculateMatrixW(Array2DRowRealMatrix B, ArrayRealVector L) {
        RealVector MatrixW = new LUDecomposition(B).getSolver().solve(L);
        return MatrixW.toArray();
    }
    public double[] solve(int n) {
        if (n<2) throw new IllegalArgumentException("Input value must be grater than 2");
        double h = 3.0/n;
        Array2DRowRealMatrix B =  GenerateMatrixB(n, h);
        ArrayRealVector L = GenerateMatrixL(n, h);
        double[] W = CalculateMatrixW(B, L);
        return W;
    }
}

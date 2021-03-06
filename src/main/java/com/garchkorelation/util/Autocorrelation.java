package com.garchkorelation.util;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Autocorrelation {

    public void print(String msg, double [] x) {
        System.out.println(msg);
        for (double d : x) System.out.println(d);
    }

    /**
     * This is a "wrapped" signal processing-style autocorrelation.
     * For "true" autocorrelation, the data must be zero padded.
     */
    public void bruteForceAutoCorrelation(double [] x, double [] ac) {
        Arrays.fill(ac, 0);
        int n = x.length;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                ac[j] += x[i] * x[(n + i - j) % n];
            }
        }
    }

    private static double sqr(double x) {
        return x * x;
    }


    public double[] fftAutoCorrelation(double [] x, double [] ac, int degree){
        for (int i=0; i<degree-1;i++){
            //ac=new double[ac.length];
            fftAutoCorrelation(x,ac);
            normalize(ac);
        }
		return ac;
    }

    public double[] fftAutoCorrelation(double [] x, double [] ac) {
        int n = x.length;
        // Assumes n is even.
        DoubleFFT_1D fft = new DoubleFFT_1D(n - 1);
        fft.realForward(x);
        ac[0] = sqr(x[0]);
        // ac[0] = 0;  // For statistical convention, zero out the mean
        ac[1] = sqr(x[1]);
        for (int i = 2; i < n - 1 ; i += 2) {
            ac[i] = sqr(x[i]) + sqr(x[i+1]);
            ac[i+1] = 0;
        }
        DoubleFFT_1D ifft = new DoubleFFT_1D(n - 1);
        ifft.realInverse(ac, true);
        // For statistical convention, normalize by dividing through with variance
        //for (int i = 1; i < n; i++)
        //    ac[i] /= ac[0];
        //ac[0] = 1;
		return ac;
    }


    public static void save(String filename, double [] x, double [] val){
        try {
            FileWriter outFile = new FileWriter(filename);
            PrintWriter printer = new PrintWriter(outFile);
            for(int i=0; i<val.length;i++){
                printer.println(x[i]+","+val[i]);
            }
            printer.close();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void normalize(double [] results){
        int n = results.length;
        for (int j = 1; j < n; j++) {
        results[j]=results[j]/results[0];
        }
        results[0]=1;
    }
    public static void normalizeOverMax(double [] results){
        int n = results.length;
        double max=Math.abs(results[0]);
        for (int j = 1; j < n; j++) {
            if(Math.abs(results[j])>max){
                max=Math.abs(results[j]);
            }
        }
        for (int j = 0; j < n; j++) {
            results[j]=results[j]/max;
        }
    }
    
    void test() {
        double [] data = { 1, -81, 2, -15, 8, 2, -9, 0};
        double [] ac1 = new double [data.length];
        double [] ac2 = new double [data.length];
        bruteForceAutoCorrelation(data, ac1);
        fftAutoCorrelation(data, ac2, data.length);
        print("bf", ac1);
        print("fft", ac2);
        double err = 0;
        for (int i = 0; i < ac1.length; i++)
            err += sqr(ac1[i] - ac2[i]);
        System.out.println("err = " + err);
    }

    public static void main(String[] args) {
        new Autocorrelation().test();
    }
}


package util.FFT;

public class FFT {
    public static  Complex[] fft(Complex[] x)
    {
        int n = x.length;
        if(n == 1)
        {
            return new Complex[]{x[0]};
        }
        if(n % 2 != 0)
        {
            return dft(x);
        }
        Complex[] even = new Complex[n/2];
        for(int k = 0;k < n / 2; k++)
        {
            even[k] = x[2*k];
        }
        Complex[] evenValue = fft(even);
        Complex[] odd = even;
        for(int k = 0;k < n/2 ;k++)
        {
            odd[k] = x[2*k + 1];
        }
        Complex[] oddValue = fft(odd);
        Complex[] result = new Complex[n];
        for(int k = 0;k < n/2; k++)
        {
            double p = -2 * k * Math.PI/n;
            Complex m = new Complex(Math.cos(p),Math.sin(p));
            result[k] = evenValue[k].plus(m.multiple(oddValue[k]));
            result[k+n/2] = evenValue[k].minus(m.multiple(oddValue[k]));

        }
        return result;
    }

    public static Complex[] dft(Complex[] x) {
        int n = x.length;

        // exp(-2i*n*PI)=cos(-2*n*PI)+i*sin(-2*n*PI)=1
        if (n == 1)
            return new Complex[]{x[0]};

        Complex[] result = new Complex[n];
        for (int i = 0; i < n; i++) {
            result[i] = new Complex(0, 0);
            for (int k = 0; k < n; k++) {
                //使用欧拉公式e^(-i*2pi*k/N) = cos(-2pi*k/N) + i*sin(-2pi*k/N)
                double p = -2 * i * k* Math.PI / n;
                Complex m = new Complex(Math.cos(p), Math.sin(p));
                result[i] = result[i].plus(x[k].multiple(m));
            }
        }
        return result;
    }

}

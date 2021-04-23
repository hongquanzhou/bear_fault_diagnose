package util.FFT;

public class Complex {
    private final double re;
    private final double im;

    public Complex(double real,double imag)
    {
        re = real;
        im = imag;
    }
    public Complex minus(Complex b)
    {
        return new Complex(this.re-b.re,this.im-b.im);
    }
    public String toString()
    {
        if(im == 0)
            return re + "";
        if(re == 0)
            return im + "i";
        if(im < 0)
            return re + "-" + (-im) + "i";
        return re + "+" + im + "i";
    }
    public double abs()
    {
        return Math.hypot(re,im);
    }
    public  Complex plus(Complex b)
    {
        Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real,imag);
    }

    public Complex multiple(double alpha)
    {
        return new Complex(alpha*re,alpha*im);
    }
    public Complex scale(double alpha)
    {
        return new Complex(alpha*re,alpha*im);
    }
    public Complex conjugate()
    {
        return new Complex(re,-im);
    }
    public Complex reciprocal()
    {
        double scale = re * re + im * im;
        return new Complex(re/scale,-im/scale);
    }
    public  double re()
    {
        return re;
    }
    public  double im()
    {
        return im;
    }
    public Complex multiple(Complex b)
    {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.im * b.re + a.re * b.im;
        return new Complex(real,imag);

    }
    public Complex divides(Complex b)
    {
        Complex a = this;
        return a.multiple(b.conjugate());
    }
    public  Complex exp()
    {
        return new Complex(Math.exp(re) * Math.cos(im),Math.exp(re)*Math.sin(im));
    }
    public Complex sin()
    {
        return new Complex(Math.sin(re) * Math.cosh(im),Math.cos(re) * Math.sinh(im));
    }
    public Complex cos()
    {
        return new Complex(Math.cos(re)*Math.cosh(im),-Math.sin(re)*Math.sinh(im));
    }
    public Complex tan()
    {
        return sin().divides(cos());
    }
    public static Complex plus(Complex a,Complex b)
    {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real,imag);
        return sum;
    }
    public  boolean equals(Object x)
    {
        if(x == null) return false;
        if(this.getClass() != x.getClass())
            return false;
        Complex that = (Complex)x;
        return (this.re == that.re)&&(this.im == that.im);
    }


}

package cn.korostudio.wtudatapresentation.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
@Slf4j
public class RGBUtil {


    final static public double param_13 = 1.0 / 3.0;
    final static public double param_16116 = 16.0 / 116.0;
    final static public double Xn = 0.950456;
    final static public double Yn = 1.0;
    final static public double Zn = 1.088754;

    public static double gamma(double x) {
        return x > 0.04045 ? Math.pow((x + 0.055f) / 1.055f, 2.4f) : (x / 12.92);
    }

    public static double gammaXYZ2RGB(double x) {
        return x > 0.0031308 ? (1.055f * Math.pow(x, (1 / 2.4f)) - 0.055) : (x * 12.92);
    }

    public static XYZ RGB2XYZ(RGB rgb) {
        XYZ xyz = new XYZ();
        double RR = gamma(rgb.R / 255.0);
        double GG = gamma(rgb.G / 255.0);
        double BB = gamma(rgb.B / 255.0);

        xyz.X = 0.4124564 * RR + 0.3575761 * GG + 0.1804375 * BB;
        xyz.Y = 0.2126729 * RR + 0.7151522 * GG + 0.0721750 * BB;
        xyz.Z = 0.0193339 * RR + 0.1191920 * GG + 0.9503041 * BB;
        return xyz;
    }

    public static Lab XYZ2Lab(XYZ xyz) {
        Lab lab = new Lab();
        double X, Y, Z;
        double dX, dY, dZ;
        X = xyz.X / (Xn);
        Y = xyz.Y / (Yn);
        Z = xyz.Z / (Zn);

        if (Y > 0.008856)
            dY = Math.pow(Y, param_13);
        else
            dY = 7.787 * Y + param_16116;

        if (X > 0.008856)
            dX = Math.pow(X, param_13);
        else
            dX = 7.787 * X + param_16116;

        if (Z > 0.008856)
            dZ = Math.pow(Z, param_13);
        else
            dZ = 7.787 * Z + param_16116;

        lab.L = 116.0 * dY - 16.0;
        lab.L = Math.max(lab.L, 0.0);
        lab.a = 500.0 * (dX - dY);
        lab.b = 200.0 * (dY - dZ);

        return lab;
    }

    public static Lab RGB2Lab(RGB rgb) {
        return XYZ2Lab(RGB2XYZ(rgb));
    }

    public static double CIE94(Lab lab1, Lab lab2) {
        double c1 = Math.sqrt(Math.pow(lab1.a, 2) + Math.pow(lab1.b, 2));
        double c2 = Math.sqrt(Math.pow(lab2.a, 2) + Math.pow(lab2.b, 2));
        double dc = c1 - c2;
        double dl = lab1.L - lab2.L;
        double da = lab1.a - lab2.a;
        double db = lab1.b - lab2.b;
        double dh = Math.sqrt(Math.pow(da, 2) + Math.pow(db, 2) - Math.pow(dc, 2));

        double sl = 1.0;
        double kc = 1.0;
        double kh = 1.0;
        double k1 = 0.045;
        double k2 = 0.015;
        double kl = 1.0;

        double deltaL = dl / (kl * sl);
        double deltaC = dc / (kc * sl);
        double deltaH = dh / (kh * sl);

        return Math.sqrt(Math.pow(deltaL, 2) + Math.pow(deltaC, 2) + Math.pow(deltaH, 2));
    }



    public static RGB XYZ2RGB(XYZ xyz) {
        RGB rgb = new RGB();
        double RR, GG, BB;
        RR = 3.2404542 * xyz.X - 1.5371385 * xyz.Y - 0.4985314 * xyz.Z;
        GG = -0.9692660 * xyz.X + 1.8760108 * xyz.Y + 0.0415560 * xyz.Z;
        BB = 0.0556434 * xyz.X - 0.2040259 * xyz.Y + 1.0572252 * xyz.Z;

        RR = gammaXYZ2RGB(RR);
        GG = gammaXYZ2RGB(GG);
        BB = gammaXYZ2RGB(BB);

        RR = (RR * 255.0 + 0.5);
        GG = (GG * 255.0 + 0.5);
        BB = (BB * 255.0 + 0.5);

        rgb.R = RR;
        rgb.G = GG;
        rgb.B = BB;
        return rgb;
    }

    public static XYZ Lab2XYZ(Lab lab) {
        XYZ xyz = new XYZ();

        double dX, dY, dZ;

        dY = (lab.L + 16.0) / 116.0;
        dX = lab.a / 500.0 + dY;
        dZ = dY - lab.b / 200.0;

        if (Math.pow(dY, 3.0) > 0.008856)
            xyz.Y = Math.pow(dY, 3.0);
        else
            xyz.Y = (dY - param_16116) / 7.787;

        if (Math.pow(dX, 3) > 0.008856)
            xyz.X = dX * dX * dX;
        else
            xyz.X = (dX - param_16116) / 7.787;

        if (Math.pow(dZ, 3.0) > 0.008856)
            xyz.Z = dZ * dZ * dZ;
        else
            xyz.Z = (dZ - param_16116) / 7.787;

        (xyz.X) *= (Xn);
        (xyz.Y) *= (Yn);
        (xyz.Z) *= (Zn);

        return xyz;
    }

    public static RGB Lab2RGB(Lab lab) {
        return XYZ2RGB(Lab2XYZ(lab));
    }

    @Data
    public static class RGB implements Serializable {
        double R;
        double G;
        double B;
    }

    @Data
    public static class XYZ implements Serializable{
        double X;
        double Y;
        double Z;

    }

    @Data
    public static class Lab implements Serializable{
        double L;
        double a;
        double b;
    }
}

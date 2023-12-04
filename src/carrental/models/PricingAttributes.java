package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import carrental.util.SerializationUtil;

public class PricingAttributes implements Serializable{
    private double weeklyDiscountRate;
    private double monthlyDiscountRate ;

    private double peakSeasonRateMultiplier;
    private int peakSeasonStartMonth;
    private int peakSeasonEndMonth;

    private double gpsServiceCharge;
    private double insuranceCharge;
    private double childSeatCharge;
    private double leatherInteriorCharge;
    private double sunroofCharge;
    private double hybridTechnologyCharge;
    private static final long serialVersionUID = -657623668606925534L;

    public PricingAttributes() {}
    public PricingAttributes(
            double weeklyDiscountRate, double monthlyDiscountRate,
            double peakSeasonRateMultiplier, int peakSeasonStartMonth, int peakSeasonEndMonth,
            double gpsServiceCharge, double insuranceCharge,
            double childSeatCharge, double leatherInteriorCharge, double sunroofCharge, double hybridTechnologyCharge) {
        this.weeklyDiscountRate = weeklyDiscountRate;
        this.monthlyDiscountRate = monthlyDiscountRate;
        this.peakSeasonRateMultiplier = peakSeasonRateMultiplier;
        this.peakSeasonStartMonth = peakSeasonStartMonth;
        this.peakSeasonEndMonth = peakSeasonEndMonth;
        this.gpsServiceCharge = gpsServiceCharge;
        this.insuranceCharge = insuranceCharge;
        this.childSeatCharge = childSeatCharge;
        this.leatherInteriorCharge = leatherInteriorCharge;
        this.sunroofCharge = sunroofCharge;
        this.hybridTechnologyCharge = hybridTechnologyCharge;
    }

    public double getWeeklyDiscountRate() {
        return weeklyDiscountRate;
    }

    public void setWeeklyDiscountRate(double defaultWeeklyDiscountRate) {
        this.weeklyDiscountRate = defaultWeeklyDiscountRate;
    }

    public double getMonthlyDiscountRate() {
        return monthlyDiscountRate;
    }

    public void setMonthlyDiscountRate(double defaultMonthlyDiscountRate) {
        this.monthlyDiscountRate = defaultMonthlyDiscountRate;
    }

    public double getPeakSeasonRateMultiplier() {
        return peakSeasonRateMultiplier;
    }

    public void setPeakSeasonRateMultiplier(double peakSeasonRateMultiplier) {
        this.peakSeasonRateMultiplier = peakSeasonRateMultiplier;
    }

    public int getPeakSeasonStartMonth() {
        return peakSeasonStartMonth;
    }

    public void setPeakSeasonStartMonth(int peakSeasonStartMonth) {
        this.peakSeasonStartMonth = peakSeasonStartMonth;
    }
    
    public int getPeakSeasonEndMonth() {
        return peakSeasonEndMonth;
    }

    public void setPeakSeasonEndMonth(int peakSeasonEndMonth) {
        this.peakSeasonEndMonth = peakSeasonEndMonth;
    }

    public double getGpsServiceCharge() {
        return gpsServiceCharge;
    }

    public void setGpsServiceCharge(double defaultGpsServiceCharge) {
        this.gpsServiceCharge = defaultGpsServiceCharge;
    }

    public double getInsuranceCharge() {
        return insuranceCharge;
    }

    public void setInsuranceCharge(double defaultInsuranceCharge) {
        this.insuranceCharge = defaultInsuranceCharge;
    }

    public double getChildSeatCharge() {
        return childSeatCharge;
    }

    public void setChildSeatCharge(double defaultChildSeatCharge) {
        this.childSeatCharge = defaultChildSeatCharge;
    }

    public double getLeatherInteriorCharge() {
        return leatherInteriorCharge;
    }

    public void setLeatherInteriorCharge(double defaultLeatherInteriorCharge) {
        this.leatherInteriorCharge = defaultLeatherInteriorCharge;
    }

    public double getSunroofCharge() {
        return sunroofCharge;
    }
    
    public void setSunroofCharge(double defaultSunroofCharge) {
        this.sunroofCharge = defaultSunroofCharge;
    }

    public double getHybridTechnologyCharge() {
        return hybridTechnologyCharge;
    }

    public void setHybridTechnologyCharge(double defaultHybridTechnologyCharge) {
        this.hybridTechnologyCharge = defaultHybridTechnologyCharge;
    }

    public void serializePricingAttributes(String filePath) {
        SerializationUtil.serializeObject(this, filePath);
    }

    public static PricingAttributes deserializePricingAttributes(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object loadedObject = inputStream.readObject();
    
            if (loadedObject instanceof PricingAttributes) {
                System.out.println("Loaded PricingAttributes ");
                return (PricingAttributes) loadedObject;
            } else {
                System.out.println("Invalid file content. Unable to deserialize PricingAttributes.");
                return null;
            }
        } catch (FileNotFoundException e) {
            // Handle the case where the file does not exist    
            return new PricingAttributes(1,1,1,6,8,1,1,1,1,1,1); // Or create a new PricingAttributes instance

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

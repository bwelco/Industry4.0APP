package comity.bawujw.citytest.model;

/**
 * Created by Sunday on 15/8/5.
 */
public class CityModel {

    /**
     * 省ID
     */
    private String provinceId;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市ID
     */
    private String cityId;

    /**
     * 市名称
     */
    private String cityName;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    @Override
    public String toString() {
        return "\n所属省ID = " + provinceId + "\n所属省名称 = " + provinceName + "\n市ID = " + cityId + "\n市名称 = " + cityName;
    }
}

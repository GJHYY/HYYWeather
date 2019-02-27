package db;
import org.litepal.crud.DataSupport;
        /*
        id 是每个实体类中都应该有的字段，
        provinceName 记录省的名字，
        provinceCode 记录省的代号
         */
public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int  provinceCode;


    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

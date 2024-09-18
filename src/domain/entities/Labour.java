package domain.entities;

import domain.enums.ComponentType;

public class Labour extends Component {
    private Integer id;
    private ComponentType componentType;
    private Double hourlyRate;
    private Double workHoursCount;
    private Double productivityRate;

    public Labour(String name, Double taxRate, Integer id, ComponentType componentType, Double hourlyRate, Double workHoursCount, Double productivityRate) {
        super(name, taxRate);
        this.id = id;
        this.componentType = componentType;
        this.hourlyRate = hourlyRate;
        this.workHoursCount = workHoursCount;
        this.productivityRate = productivityRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getWorkHoursCount() {
        return workHoursCount;
    }

    public void setWorkHoursCount(Double workHoursCount) {
        this.workHoursCount = workHoursCount;
    }

    public Double getProductivityRate() {
        return productivityRate;
    }

    public void setProductivityRate(Double productivityRate) {
        this.productivityRate = productivityRate;
    }


}

package domain.entities;


import domain.enums.ComponentType;

public class Material extends Component {
        private Integer id;
        private ComponentType componentType;
        private Double unitCost;
        private Double quantity;
        private Double transportCost;
        private Double qualityCoefficient;

        public Material(String name, Double taxRate, Integer id, ComponentType componentType, Double unitCost, Double quantity, Double transportCost, Double qualityCoefficient) {
            super(name, taxRate);
            this.id = id;
            this.componentType = componentType;
            this.unitCost = unitCost;
            this.quantity = quantity;
            this.transportCost = transportCost;
            this.qualityCoefficient = qualityCoefficient;
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

        public Double getUnitCost() {
            return unitCost;
        }

        public void setUnitCost(Double unitCost) {
            this.unitCost = unitCost;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }

        public Double getTransportCost() {
            return transportCost;
        }

        public void setTransportCost(Double transportCost) {
            this.transportCost = transportCost;
        }

        public Double getQualityCoefficient() {
            return qualityCoefficient;
        }

        public void setQualityCoefficient(Double qualityCoefficient) {
            this.qualityCoefficient = qualityCoefficient;
        }
    }

package Coding;

public class Q15_ComputerComposition {
    
    static class CPU {
        private String brand;
        private double speed;

        public CPU(String brand, double speed) {
            this.brand = brand;
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "CPU: " + brand + " " + speed + "GHz";
        }

    }

    static class RAM {
        private int size;

        public RAM(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "RAM: " + size + "GB";
        }
    }

    static class HardDrive {
        private int size;
        private String type;

        public HardDrive(int size, String type) {
            this.size = size;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Hard Drive: " + size + "GB" + type;
        }
    }

    static class Computer{
        private CPU cpu;
        private RAM ram;
        private HardDrive hardDrive;

        public Computer(String cpuBrand, double cpuSpeed, int ramSize, int hdSize, String hdType) {
            this.cpu = new CPU(cpuBrand, cpuSpeed);
            this.ram = new RAM(ramSize);
            this.hardDrive = new HardDrive(hdSize, hdType);
        }

        public String getSpecs() {
            return cpu + "\n" + ram + "\n" + hardDrive;
        }
    }

    public static void main(String[] args) {
        Computer computer = new Computer("Intel", 3.5, 16, 512, "SSD");

        System.out.println(computer.getSpecs());
    }
}
